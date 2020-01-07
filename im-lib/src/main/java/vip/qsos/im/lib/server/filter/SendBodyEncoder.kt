package vip.qsos.im.lib.server.filter

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import io.netty.util.AttributeKey
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.model.IProtobufAble
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.SessionClient
import vip.qsos.im.lib.server.model.WebSocketResponse

/**
 * @author : 华清松
 * 服务端发送指令编码
 */
class SendBodyEncoder : MessageToByteEncoder<Any>() {
    @Throws(ImException::class)
    override fun encode(ctx: ChannelHandlerContext, any: Any, out: ByteBuf) {
        val chanelType = ctx.channel()
                .attr(AttributeKey.valueOf<String>(SessionClient.CHANNEL_TYPE))
                .get()
        when {
            /** Websocket 握手数据*/
            SessionClient.WEBSOCKET == chanelType && any is WebSocketResponse -> {
                out.writeBytes(any.toString().toByteArray())
            }
            /** Websocket 业务数据*/
            SessionClient.WEBSOCKET == chanelType && any is IProtobufAble -> {
                val body = any.byteArray
                val header = createHeader(any.type, body.size)
                val protobuf = ByteArray(body.size + IMConstant.DATA_HEADER_LENGTH)
                System.arraycopy(header, 0, protobuf, 0, header.size)
                System.arraycopy(body, 0, protobuf, header.size, body.size)
                val binaryFrame = encodeDataFrame(protobuf)
                out.writeBytes(binaryFrame)
            }
            /** Protobuf 业务数据*/
            SessionClient.NATIVE_APP == chanelType && any is IProtobufAble -> {
                val body = any.byteArray
                val header = createHeader(any.type, body.size)
                out.writeBytes(header)
                out.writeBytes(body)
            }
            else -> {
                throw ImException("未识别消息类型")
            }
        }
    }

    private fun createHeader(type: Byte, length: Int): ByteArray {
        val header = ByteArray(IMConstant.DATA_HEADER_LENGTH)
        header[0] = type
        header[1] = (length and 0xff).toByte()
        header[2] = (length shr 8 and 0xff).toByte()
        return header
    }

    companion object {
        /**发送到 Websocket 的数据需要进行相关格式转换，对传入数据进行无掩码转换*/
        @Throws(ImException::class)
        fun encodeDataFrame(data: ByteArray): ByteArray {
            // 掩码开始位置
            var maskIndex = 2
            // 将数据编码放到最后
            when {
                data.size < 126 -> {
                    maskIndex = 2
                }
                data.size > 65536 -> {
                    maskIndex = 10
                }
                data.size > 125 -> {
                    maskIndex = 4
                }
            }
            // 创建返回数据
            val result = ByteArray(data.size + maskIndex)
            // 开始计算ws-frame
            // frame-fin + frame-rsv1 + frame-rsv2 + frame-rsv3 + frame-opcode
            result[0] = 0x82.toByte() // 0x82 二进制帧 0x80 文本帧
            // frame-masked+frame-payload-length
            // 从第9个字节开始是 1111101=125,掩码是第3-第6个数据
            // 从第9个字节开始是 1111110>=126,掩码是第5-第8个数据
            when {
                data.size < 126 -> {
                    result[1] = data.size.toByte()
                }
                data.size > 65536 -> {
                    result[1] = 0x7F // 127
                }
                data.size > 125 -> {
                    result[1] = 0x7E // 126
                    result[2] = (data.size shr 8).toByte()
                    result[3] = (data.size % 256).toByte()
                }
            }
            // 将数据编码放到最后
            for (i in data.indices) {
                result[i + maskIndex] = data[i]
            }
            return result
        }
    }
}