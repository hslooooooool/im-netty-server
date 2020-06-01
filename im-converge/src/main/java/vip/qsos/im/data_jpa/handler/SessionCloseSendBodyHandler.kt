package vip.qsos.im.data_jpa.handler

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.handler.IMSendBodyHandler
import vip.qsos.im.lib.server.model.IMSendBody
import vip.qsos.im.lib.server.model.IMSession
import vip.qsos.im.service.IMSessionService
import javax.annotation.Resource

/**
 * @author : 华清松
 * 连接断开处理器
 */
@Component
class SessionCloseSendBodyHandler : IMSendBodyHandler {
    @Resource
    private lateinit var IMSessionService: IMSessionService

    override fun process(sessionClient: IMSession, body: IMSendBody) {
        sessionClient.getAttribute<String>(IMConstant.KEY_ACCOUNT)?.let { account ->
            IMSessionService.find(account)?.let {
                if (!it.isApnsOpen) {
                    IMSessionService.remove(account)
                }
            }
        }
    }
}