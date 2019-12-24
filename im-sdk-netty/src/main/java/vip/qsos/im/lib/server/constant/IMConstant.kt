package vip.qsos.im.lib.server.constant

/**
 * @author : 华清松
 * 消息常量
 */
interface IMConstant {

    interface ReturnCode {
        companion object {
            const val CODE_404 = "404"
            const val CODE_403 = "403"
            const val CODE_405 = "405"
            const val CODE_200 = "200"
            const val CODE_206 = "206"
            const val CODE_500 = "500"
        }
    }

    interface ProtobufType {
        companion object {
            /**客户端心跳*/
            const val HEART_CR: Byte = 0
            /**服务端心跳*/
            const val HEART_RQ: Byte = 1
            /**消息*/
            const val MESSAGE: Byte = 2
            /**客户端消息发送*/
            const val SEND_BODY: Byte = 3
            /**服务端消息回执*/
            const val REPLY_BODY: Byte = 4
            /**会话*/
            const val SESSION: Byte = 5
            /**Websocket*/
            const val WEBSOCKET: Byte = 6
        }
    }

    companion object {
        /**【定义】消息头长度3个字节，第一个字节为消息类型，第二、三字节转换 int 后为消息长度*/
        const val DATA_HEADER_LENGTH: Int = 3

        const val KEY_ACCOUNT = "account"
        const val KEY_QUIETLY_CLOSE = "quietly_close"
        const val KEY_HEARTBEAT = "heartbeat"

        const val CLIENT_HEARTBEAT = "client_heartbeat"
        const val CLIENT_CONNECT_BIND = "client_bind"
        const val CLIENT_CONNECT_CLOSED = "client_closed"
        const val CLIENT_WEBSOCKET_HANDSHAKE = "client_websocket_handshake"
    }
}