package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import vip.qsos.im.model.BaseResult

@Api(tags = ["消息会话管理"])
@ApiSort(2)
@RequestMapping("/api/im/session")
interface ApiSession {
    @GetMapping("/list")
    fun list(): BaseResult
}