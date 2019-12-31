package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.model.BaseResult
import vip.qsos.im.service.IServerManager
import javax.annotation.Resource

@RestController
class SessionController : ApiSession {
    @Resource
    private lateinit var sessionManager: IServerManager

    override fun list(): BaseResult {
        return BaseResult.data(data = sessionManager.list())
    }
}