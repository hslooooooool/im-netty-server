package vip.qsos.im.lib.server.model

import vip.qsos.im.lib.server.config.IMConstant

/**
 * @author : 华清松
 * Websocket 响应结果实体
 */
class WebSocketResponse(private val token: String) : IProtobufAble {

    override val type: Byte = IMConstant.ProtobufType.WEBSOCKET

    override val byteArray: ByteArray
        get() = this.toString().toByteArray()

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("HTTP/1.1 101 Switching Protocols")
        builder.append("\r\n")
        builder.append("Upgrade: websocket")
        builder.append("\r\n")
        builder.append("Connection: Upgrade")
        builder.append("\r\n")
        builder.append("Sec-WebSocket-Accept:$token")
        builder.append("\r\n")
        builder.append("\r\n")
        return builder.toString()
    }

}