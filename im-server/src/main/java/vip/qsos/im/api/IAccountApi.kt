package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import vip.qsos.im.model.BaseResult

@Api(tags = ["消息账号"])
@ApiSort(0)
@RequestMapping("/api/im.account")
interface IAccountApi {
    @ApiOperation(value = "分配账号")
    @GetMapping("/assign")
    fun assign(): BaseResult

    @ApiOperation(value = "账号列表")
    @GetMapping("/list")
    fun list(
            @RequestParam
            @ApiParam(value = "账号是否被使用")
            used: Boolean? = null
    ): BaseResult
}