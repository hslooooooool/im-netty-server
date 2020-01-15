package vip.qsos.im.handler

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.SessionClientBo
import vip.qsos.im.service.SessionClientService
import javax.annotation.Resource

/**
 * @author : 华清松
 * 连接断开处理器
 */
@Component
class SessionCloseRequestHandler constructor(
        @Resource private val sessionService: SessionClientService
) : IMRequestHandler {
    override fun process(sessionClient: SessionClientBo, message: SendBody) {
        sessionClient.getAttribute<String>(IMConstant.KEY_ACCOUNT)?.let { account ->
            sessionService.find(account)?.let {
                if (!it.isApnsOpen) {
                    sessionService.remove(account)
                }
            }
        }
    }
}