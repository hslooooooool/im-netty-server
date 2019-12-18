package vip.qsos.im.lib.server.filter.decoder

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.util.AttributeKey
import vip.qsos.im.lib.model.proto.SendBodyProto
import vip.qsos.im.lib.server.constant.IMConstant
import vip.qsos.im.lib.server.model.Session
import vip.qsos.im.lib.server.model.SendBody

/**
 * @author : 华清松
 * 服务端接收来自应用的消息解码
 */
class AppMessageDecoder : ByteToMessageDecoder() {
    @Throws(Exception::class)
    public override fun decode(arg0: ChannelHandlerContext, buffer: ByteBuf, queue: MutableList<Any>) {
        /**消息长度低于3位，不做处理*/
        if (buffer.readableBytes() < IMConstant.DATA_HEADER_LENGTH) {
            return
        }
        buffer.markReaderIndex()
        /**获取消息类型*/
        val type = buffer.readByte()
        /**获取消息体长度低位*/
        val lv = buffer.readByte()
        /**获取消息体长度高位*/
        val hv = buffer.readByte()
        /**获取到消息体长度*/
        val length = getContentLength(lv.toInt(), hv.toInt())

        /**消息体没有接收完整，则重置读取，等待下一次重新读取*/
        if (length > buffer.readableBytes()) {
            buffer.resetReaderIndex()
            return
        }

        val dataBytes = ByteArray(length)
        buffer.readBytes(dataBytes)
        mappingMessageObject(dataBytes, type)?.let {
            arg0.channel().attr(AttributeKey.valueOf<Any>(Session.PROTOCOL)).set(Session.NATIVE_APP)
            queue.add(it)
        }
    }

    @Throws(Exception::class)
    fun mappingMessageObject(data: ByteArray, type: Byte): Any? {
        if (IMConstant.ProtobufType.HEART_CR == type) {
            val body = SendBody()
            body.key = IMConstant.CLIENT_HEARTBEAT
            body.timestamp = System.currentTimeMillis()
            return body
        }
        if (IMConstant.ProtobufType.SEND_BODY == type) {
            val bodyProto = SendBodyProto.Model.parseFrom(data)
            val body = SendBody()
            body.key = bodyProto.key
            body.timestamp = bodyProto.timestamp
            body.putAll(bodyProto.dataMap)
            return body
        }
        return null
    }

    /**解析消息体长度*/
    private fun getContentLength(lv: Int, hv: Int): Int {
        val l = lv and 0xff
        val h = hv and 0xff
        return l or (h shl 8)
    }

}