package com.farsunset.cim.handler

import com.farsunset.cim.service.IMSessionService
import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.constant.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.IMSession
import vip.qsos.im.lib.server.model.SendBody
import javax.annotation.Resource

/**
 * @author : 华清松
 * 断开连接，清除session
 */
@Component
class SessionClosedHandler : IMRequestHandler {
    @Resource
    private val imSessionService: IMSessionService? = null

    override fun process(session: IMSession?, message: SendBody?) {
        val quietly = session!!.getAttribute(IMConstant.KEY_QUIETLY_CLOSE)
        if (quietly == true) {
            return
        }
        val account = session.getAttribute(IMConstant.KEY_ACCOUNT) ?: return
        val oldSession = imSessionService!!.find(account.toString())
        if (oldSession == null || oldSession.isApnsOpen) {
            return
        }
        oldSession.state = IMSession.STATE_DISABLED
        oldSession.nid = null
        imSessionService.save(oldSession)
    }
}