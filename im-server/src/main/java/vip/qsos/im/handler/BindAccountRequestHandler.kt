package vip.qsos.im.handler

import org.springframework.stereotype.Component
import vip.qsos.im.config.AppConstant
import vip.qsos.im.config.AppProperties
import vip.qsos.im.dispense.MessagePusher
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.ReplyBody
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.SessionClientBo
import vip.qsos.im.model.form.SendMessageInActionForm
import vip.qsos.im.service.ChatAccountService
import vip.qsos.im.service.SessionClientService
import java.time.LocalDateTime
import javax.annotation.Resource

/**
 * @author : 华清松
 * 账号绑定实现
 */
@Component
class BindAccountRequestHandler : IMRequestHandler {
    @Resource
    private lateinit var mProperties: AppProperties
    @Resource
    private lateinit var mSessionService: SessionClientService
    @Resource
    private lateinit var mMessagePusher: MessagePusher
    @Resource
    private lateinit var mChatAccountService: ChatAccountService

    override fun process(sessionClient: SessionClientBo, message: SendBody) {
        val reply = ReplyBody()
        reply.key = message.key
        reply.code = "200"
        reply.timestamp = System.currentTimeMillis()
        reply.message = "账号绑定成功"
        try {
            val account = message.find("account") ?: throw ImException("账号不能为空")
            mChatAccountService.findByAccount(account) ?: throw ImException("账号未经授权")

            sessionClient.setAccount(account)
            sessionClient.deviceId = message.find("deviceId")
            if (sessionClient.deviceId == null || sessionClient.deviceId!!.isEmpty()) {
                throw  NullPointerException("设备ID不能为空")
            }
            sessionClient.host = mProperties.hostIp
            sessionClient.deviceType = message.find("channel")
            sessionClient.deviceModel = message.find("device")
            sessionClient.clientVersion = message.find("version")
            sessionClient.systemVersion = message.find("osVersion")
            sessionClient.bindTime = LocalDateTime.now()
            dellSession(sessionClient)
        } catch (exception: Exception) {
            reply.code = "500"
            reply.message = "账号绑定失败，${exception.message}"
        }
        sessionClient.write(reply)
    }

    private fun dellSession(sessionClient: SessionClientBo) {
        /**由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接*/
        mSessionService.find(sessionClient.getAccount())?.let { oldSession ->
            if (sameDeice(oldSession, sessionClient)) {
                /**同一个设备重复连接，如果会话通道不变则替换旧连接，否则将关闭旧链接，添加新连接*/
                if (oldSession.nid != sessionClient.nid && oldSession.isConnected) {
                    oldSession.closeOnFlush()
                }
            } else {
                /**不同设备连接，则关闭另一个终端连接，添加新连接*/
                if (oldSession.nid != sessionClient.nid && oldSession.isConnected) {
                    val msg = SendMessageInActionForm(
                            action = AppConstant.IMMessageAction.ACTION_999,
                            receiver = sessionClient.getAccount(),
                            sender = mProperties.hostName,
                            content = "您的账号在其它地方登录"
                    )
                    mMessagePusher.push(msg.getMessage())
                    oldSession.closeOnFlush()
                }
            }
        }
        /**将当前连接置入存储*/
        mSessionService.save(sessionClient)
    }

    /**判断设备ID是否一致，表示同一设备*/
    private fun sameDeice(oldSessionClient: SessionClientBo?, newSessionClient: SessionClientBo): Boolean {
        return oldSessionClient?.deviceId == newSessionClient.deviceId
    }
}