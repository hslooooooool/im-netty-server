package vip.qsos.im.lib.server.filter

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import vip.qsos.im.lib.model.proto.SendBodyProto
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.utils.DataUtils
import kotlin.experimental.xor

/**
 * @author : 华清松
 * 客户端发送的消息解码 WebSocket 版本
 */
class WebMessageDecoder : ByteToMessageDecoder() {
    @Throws(ImException::class)
    public override fun decode(arg0: ChannelHandlerContext, buffer: ByteBuf, queue: MutableList<Any>) {
        buffer.markReaderIndex()
        /**判断 fin 标志位是否是1 如果是0 则等待消息接收完成*/
        val tag = buffer.readByte()
        /**有符号 byte 第一位为1则为负数 第一位为0则为正数，以此 判断 fin 字段是 0 还是 1*/
        if (tag > 0) {
            /**等待消息接收完成，解除 mark*/
            buffer.resetReaderIndex()
            return
        } else {
            /**获取帧操作码。在 protobuf 中，仅支持二进制帧 OPCODE_BINARY，以及客户端关闭连接帧通知 OPCODE_CLOSE*/
            val opcode: Int = tag.toInt() and 0x0F
            if (0x2 == opcode) {
                /**二进制消息*/
                val head = buffer.readByte().toInt()
                val dataLength = head and 0x7F
                /**数据长度*/
                val realLength: Int
                /**
                 * 数据承载能力， Payload length === x，如果：
                 * x为0~126：数据的长度为x字节。
                 * x为126：后续2个字节代表一个16位的无符号整数，该无符号整数的值为数据的长度。
                 * x为127：后续8个字节代表一个64位的无符号整数（最高位为0），该无符号整数的值为数据的长度。
                 */
                realLength = when (dataLength) {
                    126 -> {
                        buffer.readShort().toInt()
                    }
                    127 -> {
                        buffer.readLong().toInt()
                    }
                    else -> {
                        dataLength
                    }
                }
                /**判断是否有4位数的掩码，无掩码关闭连接，客户端发送到服务端的数据帧，Mask都是1*/
                val masked = head shr 7 and 0x1 == 1
                if (masked) {
                    val mask = ByteArray(4)
                    buffer.readBytes(mask)
                    if (buffer.readableBytes() > 0) {
                        /**收到指令，获取掩码后有可读数据*/
                        val data = ByteArray(realLength)
                        buffer.readBytes(data)
                        for (i in 0 until realLength) {
                            data[i] = (data[i] xor mask[i % 4])
                        }
                        handleMessage(data, queue)
                    } else {
                        /**收到指令，获取掩码后无可读数据*/
                        buffer.resetReaderIndex()
                    }
                } else {
                    /**收到指令，无掩码，断开连接*/
                    handleSocketClosed(arg0, buffer)
                }
            } else if (0x8 == opcode) {
                /**收到关闭指令，关闭连接*/
                handleSocketClosed(arg0, buffer)
            } else {
                /**收到未知指令，忽略其他类型，清除此次读取数据*/
                buffer.readBytes(ByteArray(buffer.readableBytes()))
            }
        }
    }

    /**客户端主动断开连接*/
    private fun handleSocketClosed(arg0: ChannelHandlerContext, buffer: ByteBuf) {
        /**清除此次读取数据*/
        buffer.readBytes(ByteArray(buffer.readableBytes()))
        arg0.channel().close()
    }

    /**客户端发送的消息*/
    @Throws(ImException::class)
    fun handleMessage(data: ByteArray, queue: MutableList<Any>) {
        when (data[0]) {
            IMConstant.ProtobufType.HEART_CR -> {
                val body = SendBody()
                body.key = IMConstant.CLIENT_HEARTBEAT
                body.timestamp = System.currentTimeMillis()
                queue.add(body)
            }
            IMConstant.ProtobufType.SEND_BODY -> {
                val length = DataUtils.getContentLength(data[1].toInt(), data[2].toInt())
                val protobuf = ByteArray(length)
                System.arraycopy(data, IMConstant.DATA_HEADER_LENGTH, protobuf, 0, length)
                val bodyProto = SendBodyProto.Model.parseFrom(protobuf)
                val body = SendBody()
                body.key = bodyProto.key
                body.timestamp = bodyProto.timestamp
                body.putAll(bodyProto.dataMap)
                queue.add(body)
            }
            /**其它消息，不必解析*/
            else -> {

            }
        }
    }
}