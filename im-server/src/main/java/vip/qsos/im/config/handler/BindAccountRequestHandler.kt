package vip.qsos.im.config.handler

import org.springframework.stereotype.Component
import vip.qsos.im.config.AppConstant
import vip.qsos.im.config.AppProperties
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.lib.server.model.ReplyBody
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.Session
import vip.qsos.im.component.IMessagePusher
import vip.qsos.im.service.IServerManager
import javax.annotation.Resource

/**
 * @author : 华清松
 * 账号绑定实现
 */
@Component
class BindAccountRequestHandler constructor(
        @Resource private val mProperties: AppProperties,
        @Resource private val sessionManager: IServerManager,
        @Resource private val defaultMessagePusher: IMessagePusher
) : IMRequestHandler {
    override fun process(session: Session, message: SendBody) {
        val reply = ReplyBody()
        reply.key = message.key
        reply.code = "200"
        reply.timestamp = System.currentTimeMillis()
        reply.message = "账号绑定成功"
        try {
            val account = message.find("account")
            session.setAccount(account)
            session.deviceId = message.find("deviceId")
            if (session.deviceId == null || session.deviceId!!.isEmpty()) {
                throw  NullPointerException("设备ID不能为空")
            }
            session.host = mProperties.hostIp
            session.deviceType = message.find("channel")
            session.deviceModel = message.find("device")
            session.clientVersion = message.find("version")
            session.systemVersion = message.find("osVersion")
            session.bindTime = System.currentTimeMillis()
            dellSession(session)
        } catch (exception: Exception) {
            reply.code = "500"
            reply.message = "账号绑定失败，${exception.message}"
        }
        session.write(reply)
    }

    private fun dellSession(session: Session) {
        /**由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接*/
        if (null == session.getAccount()) {
            throw NullPointerException("账号不能为空")
        }
        sessionManager.find(session.getAccount()!!)?.let { oldSession ->
            if (sameDeice(oldSession, session)) {
                /**同一个设备重复连接，如果会话通道不变则替换旧连接，否则将关闭旧链接，添加新连接*/
                if (oldSession.nid != session.nid && oldSession.isConnected) {
                    oldSession.closeOnFlush()
                }
            } else {
                /**不同设备连接，则关闭另一个终端连接，添加新连接*/
                if (oldSession.nid != session.nid && oldSession.isConnected) {
                    val msg = Message()
                    msg.id = System.currentTimeMillis()
                    msg.action = AppConstant.IMMessageAction.ACTION_999
                    msg.receiver = session.getAccount()
                    msg.sender = mProperties.hostName
                    msg.content = "您的账号在其它地方登录"
                    defaultMessagePusher.push(msg)
                    oldSession.closeOnFlush()
                }
            }
            sessionManager.remove(oldSession.getAccount()!!)
        }
        /**将当前连接置入存储*/
        sessionManager.save(session)
    }

    /**判断设备ID是否一致，表示同一设备*/
    private fun sameDeice(oldSession: Session?, newSession: Session): Boolean {
        return oldSession?.deviceId == newSession.deviceId
    }
}