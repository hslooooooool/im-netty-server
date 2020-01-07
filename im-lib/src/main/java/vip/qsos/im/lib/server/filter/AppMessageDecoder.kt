package vip.qsos.im.lib.server.filter

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.util.AttributeKey
import vip.qsos.im.lib.model.proto.SendBodyProto
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.SessionClient

/**
 * @author : 华清松
 * 服务端接收来自应用的消息解码 Socket 版本
 */
class AppMessageDecoder : ByteToMessageDecoder() {
    @Throws(ImException::class)
    public override fun decode(arg0: ChannelHandlerContext, buffer: ByteBuf, queue: MutableList<Any>) {
        arg0.channel().attr(AttributeKey.valueOf<String>(SessionClient.CHANNEL_TYPE)).set(SessionClient.NATIVE_APP)
        while (buffer.readableBytes() > IMConstant.DATA_HEADER_LENGTH) {
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
            val readableLen = bodyByteBuf.readableBytes()
            var content: ByteArray
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
    @Throws(ImException::class)
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
            else -> throw ImException("无法解析的消息")
        }
    }

}