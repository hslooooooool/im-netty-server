package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.model.BaseResult
import vip.qsos.im.service.SessionClientManager
import javax.annotation.Resource

@RestController
class SessionClientController : ApiSessionClient {
    @Resource
    private lateinit var sessionManager: SessionClientManager

    override fun list(): BaseResult {
        return BaseResult.data(data = sessionManager.list())
    }
}