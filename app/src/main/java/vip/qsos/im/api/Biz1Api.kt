package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import vip.qsos.im.model.BaseResult

@Api(tags = ["APP主页"])
@ApiSort(3)
@RequestMapping("/api/app/biz1")
interface Biz1Api {

    @ApiOperation(value = "会话列表")
    @GetMapping("/list.session")
    fun sessionList(
            @RequestParam
            @ApiParam(value = "用户ID", required = true)
            userId: Long
    ): BaseResult

    @ApiOperation(value = "好友列表")
    @GetMapping("/list.friend")
    fun friendList(
            @RequestParam
            @ApiParam(value = "用户ID", required = true)
            userId: Long
    ): BaseResult

}