package com.farsunset.cim.handler

import com.farsunset.cim.service.IMSessionService
import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.constant.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.Session
import javax.annotation.Resource

/**
 * @author : 华清松
 * 断开连接，清除session
 */
@Component
class SessionCloseRequestHandler : IMRequestHandler {
    @Resource
    private val imSessionService: IMSessionService? = null

    override fun process(session: Session?, message: SendBody?) {
        session?.getAttribute(IMConstant.KEY_ACCOUNT)?.let { account ->
            account as String
            imSessionService!!.find(account)?.let {
                if (!it.isApnsOpen) {
                    imSessionService.remove(account)
                }
            }
        }
    }
}