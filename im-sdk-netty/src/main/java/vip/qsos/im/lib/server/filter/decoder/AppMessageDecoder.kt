package vip.qsos.im.lib.server.filter.decoder

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.util.AttributeKey
import vip.qsos.im.lib.model.proto.SendBodyProto
import vip.qsos.im.lib.server.constant.IMConstant
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.Session

/**
 * @author : 华清松
 * 服务端接收来自应用的消息解码
 */
class AppMessageDecoder : ByteToMessageDecoder() {
    @Throws(Exception::class)
    public override fun decode(arg0: ChannelHandlerContext, buffer: ByteBuf, queue: MutableList<Any>) {
        arg0.channel().attr(AttributeKey.valueOf<Any>(Session.CHANNEL_TYPE)).set(Session.NATIVE_APP)
        while (buffer.readableBytes() > 4) {
            // 如果可读长度小于包头长度，退出。
            buffer.markReaderIndex()
            // 获取类型
            val dataType: Byte = buffer.readByte()
            // 获取包头中的body长度
            val low: Int = buffer.readByte().toInt()
            val high: Int = buffer.readByte().toInt()
            val s0 = low and 0xff
            var s1 = high and 0xff
            s1 = s1 shl 8
            val bodyLength = s0 or s1
            // 可读长度小于body长度，恢复读指针，退出。
            if (buffer.readableBytes() < bodyLength) {
                buffer.resetReaderIndex()
                return
            }
            // 读取消息内容
            val bodyByteBuf: ByteBuf = buffer.readBytes(bodyLength)
            var content: ByteArray
            val readableLen = bodyByteBuf.readableBytes()
            if (bodyByteBuf.hasArray()) {
                content = bodyByteBuf.array()
            } else {
                content = ByteArray(readableLen)
                bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), content, 0, readableLen)
            }

            queue.add(decodeBody(dataType, content))
        }

    }

    /**解析消息内容*/
    @Throws(Exception::class)
    fun decodeBody(type: Byte, array: ByteArray): SendBody {
        return when (type) {
            IMConstant.ProtobufType.HEART_CR -> {
                SendBody().also {
                    it.key = IMConstant.CLIENT_HEARTBEAT
                    it.timestamp = System.currentTimeMillis()
                }
            }
            IMConstant.ProtobufType.SEND_BODY -> {
                SendBodyProto.Model.parseFrom(array).let { model ->
                    SendBody().also {
                        it.key = IMConstant.CLIENT_BIND
                        it.timestamp = System.currentTimeMillis()
                        it.putAll(model.dataMap)
                    }
                }
            }
            else -> throw NullPointerException("无法解析的消息")
        }
    }

}