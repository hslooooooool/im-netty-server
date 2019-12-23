package vip.qsos.im.lib.server.filter.decoder

import com.google.protobuf.InvalidProtocolBufferException
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import vip.qsos.im.lib.model.proto.SendBodyProto
import vip.qsos.im.lib.server.constant.IMConstant
import vip.qsos.im.lib.server.model.SendBody
import kotlin.experimental.xor

/**
 * @author : 华清松
 * 客户端发送的消息解码
 */
class WebMessageDecoder : ByteToMessageDecoder() {

    @Throws(Exception::class)
    public override fun decode(arg0: ChannelHandlerContext, buffer: ByteBuf, queue: MutableList<Any>) {
        buffer.markReaderIndex()
        /**判断 fin 标志位是否是1 如果是0 则等待消息接收完成*/
        val tag = buffer.readByte()
        println("收到指令：$buffer")
        /**有符号byte 第一位为1则为负数 第一位为0则为正数，以此 判断 fin 字段是 0 还是 1*/
        if (tag > 0) {
            /**等待消息接收完成，解除 mark*/
            buffer.resetReaderIndex()
            return
        } else {

            /**获取帧操作码。在 protobuf 中，仅支持二进制帧 OPCODE_BINARY，以及客户端关闭连接帧通知 OPCODE_CLOSE*/
            val frameOpcode: Int = tag.toInt() and TAG_MASK.toInt()
            if (OPCODE_BINARY.toInt() == frameOpcode) {
                /**二进制消息*/
                val head = buffer.readByte().toInt()
                val dataLength = (head and PAY_LOAD_LEN.toInt()).toByte()
                val realLength: Int
                /**
                 * 数据承载能力，7位或者7+16位或者7+64位，表示数据帧中数据大小，有以下情况。
                 * 如果值为0-125，那么该值就是payload data的真实长度。
                 * 如果值为126，那么该7位后面紧跟着的2个字节就是payload data的真实长度。
                 * 如果值为127，那么该7位后面紧跟着的8个字节就是payload data的真实长度。
                 */
                realLength = when (dataLength) {
                    HAS_EXTEND_DATA -> {
                        buffer.readShort().toInt()
                    }
                    HAS_EXTEND_DATA_CONTINUE -> {
                        buffer.readLong().toInt()
                    }
                    else -> {
                        dataLength.toInt()
                    }
                }
                val masked = head shr 7 and MASK.toInt() == 1
                if (masked) {
                    // 有掩码，获取掩码
                    val mask = ByteArray(4)
                    buffer.readBytes(mask)
                    if (buffer.readableBytes() > 0) {
                        val data = ByteArray(realLength)
                        buffer.readBytes(data)
                        for (i in 0 until realLength) {
                            // 数据进行异或运算
                            data[i] = (data[i] xor mask[i % 4])
                        }
                        handleMessage(data, queue)
                    }else{
                        println("收到指令：$buffer")
                    }
                }else{
                    println("收到指令：$buffer")
                }
            } else if (OPCODE_CLOSE.toInt() == frameOpcode) {
                /**关闭连接消息*/
                handleSocketClosed(arg0, buffer)
            } else {
                /**忽略其他类型的消息，清除此次读取数据*/
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
    @Throws(InvalidProtocolBufferException::class)
    fun handleMessage(data: ByteArray, queue: MutableList<Any>) {
        when (data[0]) {
            /**客户端心跳响应*/
            IMConstant.ProtobufType.HEART_CR -> {
                val body = SendBody()
                body.key = IMConstant.CLIENT_HEARTBEAT
                body.timestamp = System.currentTimeMillis()
                queue.add(body)
            }
            /**客户端消息*/
            IMConstant.ProtobufType.SEND_BODY -> {
                val length = getContentLength(data[1].toInt(), data[2].toInt())
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

    /**解析消息体长度*/
    private fun getContentLength(lv: Int, hv: Int): Int {
        val l = lv and 0xff
        val h = hv and 0xff
        return l or (h shl 8)
    }

    companion object {
        const val MASK: Byte = 0x1 // 1000 0000
        const val HAS_EXTEND_DATA: Byte = 126
        const val HAS_EXTEND_DATA_CONTINUE: Byte = 127
        const val PAY_LOAD_LEN: Byte = 0x7F // 0111 1111 = 127
        const val TAG_MASK: Byte = 0x0F // 0000 1111 = 15
        private const val OPCODE_BINARY: Byte = 0x2
        private const val OPCODE_CLOSE: Byte = 0x8
    }
}