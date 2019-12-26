package vip.qsos.im.lib.server.constant

/**
 * @author : 华清松
 * 消息常量
 */
interface IMConstant {

    /**消息类型*/
    interface ProtobufType {
        companion object {
            /**【定义】客户端心跳*/
            const val HEART_CR: Byte = 0
            /**【定义】服务端心跳*/
            const val HEART_RQ: Byte = 1
            /**【定义】消息*/
            const val MESSAGE: Byte = 2
            /**【定义】客户端消息发送*/
            const val SEND_BODY: Byte = 3
            /**【定义】服务端消息回执*/
            const val REPLY_BODY: Byte = 4
            /**【定义】会话*/
            const val SESSION: Byte = 5
            /**【定义】Websocket*/
            const val WEBSOCKET: Byte = 6
        }
    }

    companion object {
        /**【定义】消息头长度3个字节，第一个字节为消息类型，第二、三字节转换 int 后为消息长度*/
        const val DATA_HEADER_LENGTH: Int = 3

        /**【定义】保存于Channel中的登录账号*/
        const val KEY_ACCOUNT = "account"
        /**【定义】保存于Channel中的上一次心跳时间戳*/
        const val KEY_LAST_HEARTBEAT_TIME = "last_heartbeat_time"

        /**【定义】心跳请求*/
        const val CLIENT_HEARTBEAT = "client_heartbeat"
        /**【定义】账号绑定请求*/
        const val CLIENT_BIND = "client_bind"
        /**【定义】连接关闭*/
        const val CLIENT_CLOSED = "client_closed"
        /**【定义】Websocket 握手*/
        const val CLIENT_HANDSHAKE = "client_websocket_handshake"
        /**【定义】APP 业务处理*/
        const val CLIENT_APP_CUSTOM = "client_app_custom"
    }
}