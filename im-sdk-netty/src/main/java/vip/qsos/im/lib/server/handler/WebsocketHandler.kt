package vip.qsos.im.lib.server.handler

import vip.qsos.im.lib.server.model.Session
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.WebSocketResponse
import java.security.MessageDigest
import java.util.*

/**
 * @author : 华清松
 * 处理 websocket 握手请求，返回响应的报文给浏览器
 */
class WebsocketHandler : IMRequestHandler {

    companion object {
        private const val GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"
    }

    override fun process(session: Session?, message: SendBody?) {
        session!!.deviceType = Session.CHANNEL_BROWSER
        var secKey = message!!.find("key") + GUID
        try {
            val md = MessageDigest.getInstance("SHA-1")
            md.update(secKey.toByteArray(charset("iso-8859-1")), 0, secKey.length)
            val sha1Hash = md.digest()
            secKey = String(Base64.getEncoder().encode(sha1Hash))
        } catch (ignore: Exception) {
        }
        session.write(WebSocketResponse(secKey))
    }
}