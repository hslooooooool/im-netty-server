package vip.qsos.im.handler

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.IMSession
import vip.qsos.im.service.IMSessionService
import javax.annotation.Resource

/**
 * @author : 华清松
 * 连接断开处理器
 */
@Component
class SessionCloseRequestHandler : IMRequestHandler {
    @Resource
    private lateinit var IMSessionService: IMSessionService

    override fun process(sessionClient: IMSession, message: SendBody) {
        sessionClient.getAttribute<String>(IMConstant.KEY_ACCOUNT)?.let { account ->
            IMSessionService.find(account)?.let {
                if (!it.isApnsOpen) {
                    IMSessionService.remove(account)
                }
            }
        }
    }
}