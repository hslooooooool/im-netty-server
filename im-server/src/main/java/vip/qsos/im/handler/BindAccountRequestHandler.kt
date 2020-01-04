package vip.qsos.im.handler

import org.springframework.stereotype.Component
import vip.qsos.im.component.IMessagePusher
import vip.qsos.im.config.AppConstant
import vip.qsos.im.config.AppProperties
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.ReplyBody
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.Session
import vip.qsos.im.model.form.SendMessageInActionForm
import vip.qsos.im.repository.AccountRepository
import vip.qsos.im.service.IServerManager
import java.time.LocalDateTime
import javax.annotation.Resource

/**
 * @author : 华清松
 * 账号绑定实现
 */
@Component
class BindAccountRequestHandler constructor(
        @Resource private val mProperties: AppProperties,
        @Resource private val mSessionManager: IServerManager,
        @Resource private val mMessagePusher: IMessagePusher,
        @Resource private val mAccountRepository: AccountRepository
) : IMRequestHandler {
    override fun process(session: Session, message: SendBody) {
        val reply = ReplyBody()
        reply.key = message.key
        reply.code = "200"
        reply.timestamp = System.currentTimeMillis()
        reply.message = "账号绑定成功"
        try {
            val account = message.find("account") ?: throw ImException("账号不能为空")
            mAccountRepository.findByAccount(account) ?: throw ImException("账号未经授权")

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
            session.bindTime = LocalDateTime.now()
            dellSession(session)
        } catch (exception: Exception) {
            reply.code = "500"
            reply.message = "账号绑定失败，${exception.message}"
        }
        session.write(reply)
    }

    private fun dellSession(session: Session) {
        /**由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接*/
        mSessionManager.find(session.getAccount())?.let { oldSession ->
            if (sameDeice(oldSession, session)) {
                /**同一个设备重复连接，如果会话通道不变则替换旧连接，否则将关闭旧链接，添加新连接*/
                if (oldSession.nid != session.nid && oldSession.isConnected) {
                    oldSession.closeOnFlush()
                }
            } else {
                /**不同设备连接，则关闭另一个终端连接，添加新连接*/
                if (oldSession.nid != session.nid && oldSession.isConnected) {
                    val msg = SendMessageInActionForm(
                            action = AppConstant.IMMessageAction.ACTION_999,
                            receiver = session.getAccount(),
                            sender = mProperties.hostName,
                            content = "您的账号在其它地方登录"
                    )
                    mMessagePusher.push(msg.getMessage())
                    oldSession.closeOnFlush()
                }
            }
        }
        /**将当前连接置入存储*/
        mSessionManager.save(session)
    }

    /**判断设备ID是否一致，表示同一设备*/
    private fun sameDeice(oldSession: Session?, newSession: Session): Boolean {
        return oldSession?.deviceId == newSession.deviceId
    }
}