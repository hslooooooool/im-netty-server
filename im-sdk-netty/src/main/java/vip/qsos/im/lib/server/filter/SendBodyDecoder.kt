package vip.qsos.im.lib.server.filter

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.util.AttributeKey
import vip.qsos.im.lib.server.IMConstant
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.Session
import java.util.regex.Pattern

/**
 * @author : 华清松
 * 服务端接收指令解码，通过消息类型分发到不同的解码器
 */
class SendBodyDecoder : ByteToMessageDecoder() {

    companion object {
        /**【正则】获取 websocket 握手消息中的 Sec-WebSocket-Key*/
        val SEC_KEY_PATTERN: Pattern = Pattern.compile("^(Sec-WebSocket-Key:).+", Pattern.CASE_INSENSITIVE or Pattern.MULTILINE)
        /**【正则】获取 websocket 握手消息中的 Upgrade 值*/
        val UPGRADE_PATTERN: Pattern = Pattern.compile("^(Upgrade:).+", Pattern.CASE_INSENSITIVE or Pattern.MULTILINE)
    }

    /**网页消息解析器*/
    private val webMessageDecoder: WebMessageDecoder = WebMessageDecoder()
    /**APP消息解析器*/
    private val appMessageDecoder: AppMessageDecoder = AppMessageDecoder()

    @Throws(ImException::class)
    override fun decode(context: ChannelHandlerContext, buffer: ByteBuf, queue: MutableList<Any>) {
        when (context.channel().attr(AttributeKey.valueOf<String>(Session.CHANNEL_TYPE)).get()) {
            Session.WEBSOCKET -> {
                webMessageDecoder.decode(context, buffer, queue)
            }
            Session.NATIVE_APP -> {
                appMessageDecoder.decode(context, buffer, queue)
            }
            else -> {
                val handshake = tryWebsocketHandleHandshake(context, buffer, queue)
                if (!handshake) {
                    appMessageDecoder.decode(context, buffer, queue)
                }
            }
        }
    }

    /**尝试解析为 web 消息，失败后解析为 app 消息*/
    @Throws(ImException::class)
    private fun tryWebsocketHandleHandshake(arg0: ChannelHandlerContext, buffer: ByteBuf, queue: MutableList<Any>): Boolean {
        buffer.markReaderIndex()
        val data = ByteArray(buffer.readableBytes())
        buffer.readBytes(data)
        val request = String(data)
        val secKey = getSecWebSocketKey(request)
        val handShake = secKey != null && getUpgradeProtocol(request) == Session.WEBSOCKET
        if (handShake) {
            /**握手响应之后，标记当前 session 的协议为 websocket */
            arg0.channel().attr(AttributeKey.valueOf<String>(Session.CHANNEL_TYPE)).set(Session.WEBSOCKET)
            val body = SendBody()
            body.key = IMConstant.CLIENT_HANDSHAKE
            body.timestamp = System.currentTimeMillis()
            body.put("key", secKey)
            queue.add(body)
        } else {
            buffer.resetReaderIndex()
        }
        return handShake
    }

    /**通过正则获取 websocket 握手消息中的 Sec-WebSocket-Key*/
    private fun getSecWebSocketKey(message: String): String? {
        val m = SEC_KEY_PATTERN.matcher(message)
        return if (m.find()) {
            m.group().split(":".toRegex()).toTypedArray()[1].trim { it <= ' ' }
        } else null
    }

    /**通过正则获取 websocket 握手消息中的 Upgrade 值，预期为 websocket*/
    private fun getUpgradeProtocol(message: String): String? {
        val m = UPGRADE_PATTERN.matcher(message)
        return if (m.find()) {
            m.group().split(":".toRegex()).toTypedArray()[1].trim { it <= ' ' }
        } else null
    }

}