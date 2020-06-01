package vip.qsos.im.lib.server.model

/**Websocket 响应结果实体
 * @author : 华清松
 */
class WebSocketResponse(private val token: String) {

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