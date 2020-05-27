package vip.qsos.im.lib.server.handler

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.SessionClientBo
import vip.qsos.im.lib.server.model.WebSocketResponse
import java.security.MessageDigest
import java.util.*

/**处理 websocket 握手请求，返回响应的报文给浏览器
 * @author : 华清松
 */
@Component
class WebsocketHandShakeHandler : IMRequestHandler {

    companion object {
        private const val GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"
    }

    override fun process(sessionClient: SessionClientBo, message: SendBody) {
        sessionClient.deviceType = SessionClientBo.CHANNEL_BROWSER
        var secKey = message.find("key") + GUID
        try {
            val md = MessageDigest.getInstance("SHA-1")
            md.update(secKey.toByteArray(charset("iso-8859-1")), 0, secKey.length)
            val sha1Hash = md.digest()
            secKey = String(Base64.getEncoder().encode(sha1Hash))
        } catch (ignore: Exception) {
        }
        sessionClient.write(WebSocketResponse(secKey))
    }
}