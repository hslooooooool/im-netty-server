package vip.qsos.im.handler

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.constant.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.Session
import vip.qsos.im.service.IServerManager
import javax.annotation.Resource

/**
 * @author : 华清松
 * 连接断开处理器
 */
@Component
class SessionCloseRequestHandler constructor(
        @Resource private val sessionManager: IServerManager
) : IMRequestHandler {
    override fun process(session: Session?, message: SendBody?) {
        session?.getAttribute<String>(IMConstant.KEY_ACCOUNT)?.let { account ->
            sessionManager.find(account)?.let {
                if (!it.isApnsOpen) {
                    sessionManager.remove(account)
                }
            }
        }
    }
}