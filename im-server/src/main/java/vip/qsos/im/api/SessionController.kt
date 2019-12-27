package vip.qsos.im.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.model.BaseResult
import vip.qsos.im.service.IServerManager
import javax.annotation.Resource

@RestController
@RequestMapping("/api/session")
class SessionController constructor(
        @Resource private val sessionManager: IServerManager
) {

    @GetMapping("/list")
    fun list(): BaseResult {
        return BaseResult.data(sessionManager.list())
    }
}