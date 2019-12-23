package com.farsunset.cim.handler

import com.farsunset.cim.AppConstant
import com.farsunset.cim.push.IMMessagePusher
import com.farsunset.cim.service.IMSessionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.constant.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.lib.server.model.ReplyBody
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.Session
import javax.annotation.Resource

/**
 * @author : 华清松
 * 账号绑定实现
 */
@Component
class BindAccountRequestHandler : IMRequestHandler {

    private val logger = LoggerFactory.getLogger(BindAccountRequestHandler::class.java)

    @Resource
    private val imSessionService: IMSessionService? = null
    @Value("\${im.server.host.ip}")
    private val host: String? = null
    @Resource
    private val defaultMessagePusher: IMMessagePusher? = null

    override fun process(session: Session?, message: SendBody?) {
        val reply = ReplyBody()
        reply.key = message!!.key
        reply.code = IMConstant.ReturnCode.CODE_200
        reply.timestamp = System.currentTimeMillis()
        try {
            val account = message["account"]
            session!!.setAccount(account)
            session.deviceId = message["deviceId"]
            session.host = host
            session.channel = message["channel"]
            session.deviceModel = message["device"]
            session.clientVersion = message["version"]
            session.systemVersion = message["osVersion"]
            session.bindTime = System.currentTimeMillis()
            /**由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接*/
            val oldSession = imSessionService!!.find(account)
            /**如果是账号已经在另一台终端登录。则让另一个终端下线*/
            if (oldSession != null && fromOtherDevice(oldSession, session) && oldSession.isConnected) {
                sendForceOfflineMessage(oldSession, account, session.deviceModel)
            }
            /**
             * 有可能是同一个设备重复连接，则关闭旧的链接，这种情况一般是客户端断网，联网又重新链接上来，之前的旧链接没有来得及通过心跳机制关闭，在这里手动关闭
             * 条件1，连接来自是同一个设备
             * 条件2.2个连接都是同一台服务器
             */
            if (oldSession != null && !fromOtherDevice(oldSession, session) && oldSession.host == host) {
                closeQuietly(oldSession)
            }
            imSessionService.save(session)
        } catch (exception: Exception) {
            reply.code = IMConstant.ReturnCode.CODE_500
            logger.error("Bind has error", exception)
        }
        session!!.write(reply)
    }

    private fun fromOtherDevice(oldSession: Session?, newSession: Session): Boolean {
        return oldSession!!.deviceId != newSession.deviceId
    }

    private fun sendForceOfflineMessage(oldSession: Session, account: String?, deviceModel: String?) {
        val msg = Message()
        msg.action = AppConstant.IMMessageAction.ACTION_999
        msg.receiver = account
        msg.sender = "system"
        msg.content = deviceModel
        msg.id = System.currentTimeMillis()
        defaultMessagePusher!!.push(msg)
        closeQuietly(oldSession)
    }

    /**不同设备同一账号登录时关闭旧的连接*/
    private fun closeQuietly(oldSession: Session) {
        if (oldSession.isConnected && host == oldSession.host) {
            oldSession.setAttribute(IMConstant.KEY_QUIETLY_CLOSE, true)
            oldSession.closeOnFlush()
        }
    }
}