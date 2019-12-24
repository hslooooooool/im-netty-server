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
        // TODO 判断host转发请求到其它服务器

        val reply = ReplyBody()
        reply.key = message!!.key
        reply.code = IMConstant.ReturnCode.CODE_200
        reply.timestamp = System.currentTimeMillis()
        try {
            val account = message["account"]
            session!!.setAccount(account)
            session.deviceId = message["deviceId"]
            if (session.deviceId == null || session.deviceId!!.isEmpty()) {
                throw  NullPointerException("设备ID不能为空")
            }
            session.host = host
            session.channel = message["channel"]
            session.deviceModel = message["device"]
            session.clientVersion = message["version"]
            session.systemVersion = message["osVersion"]
            session.bindTime = System.currentTimeMillis()
            session.setAttribute(IMConstant.KEY_QUIETLY_CLOSE, false)
            dellSession(session)
        } catch (exception: Exception) {
            reply.code = IMConstant.ReturnCode.CODE_500
            reply.message = "账号绑定失败"
            logger.error("Bind has error", exception)
        }
        session!!.write(reply)
    }

    private fun dellSession(session: Session) {
        /**由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接*/
        imSessionService!!.find(session.getAccount())?.let { oldSession ->
            if (sameDeice(oldSession, session)) {
                /**同一个设备重复连接，如果会话通道不变则替换旧连接，否则将关闭旧链接，添加新连接*/
                if (oldSession.nid != session.nid && oldSession.isConnected) {
                    oldSession.closeOnFlush()
                }
                imSessionService.remove(oldSession.getAccount())
            } else {
                /**不同设备连接，则关闭另一个终端连接，添加新连接*/
                if (oldSession.nid != session.nid && oldSession.isConnected) {
                    val msg = Message()
                    msg.id = System.currentTimeMillis()
                    msg.action = AppConstant.IMMessageAction.ACTION_999
                    msg.receiver = session.getAccount()
                    msg.sender = "system"
                    msg.content = "您的账号在其它地方登录"
                    defaultMessagePusher!!.push(msg)
                    oldSession.closeOnFlush()
                }
                imSessionService.remove(oldSession.getAccount())
            }
        }
        /**将当前连接置入存储*/
        imSessionService.save(session)
    }

    /**不同设备同一账号登录时关闭旧的连接*/
    private fun closeQuietly(oldSession: Session, newSession: Session) {
        if (oldSession.isConnected && host == oldSession.host) {
            oldSession.setAttribute(IMConstant.KEY_QUIETLY_CLOSE, true)
            if (oldSession.nid != newSession.nid) {
                oldSession.closeOnFlush()
            }
            imSessionService!!.remove(oldSession.getAccount())
        }
    }

    /**判断设备ID是否一致，表示同一设备*/
    private fun sameDeice(oldSession: Session?, newSession: Session): Boolean {
        return oldSession?.deviceId == newSession.deviceId
    }
}