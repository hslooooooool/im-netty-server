package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import vip.qsos.im.model.BaseResult

@Api(tags = ["消息会话管理"])
@ApiSort(2)
@RequestMapping("/api/im/session")
interface SessionApi {

    @ApiOperation(value = "获取单聊会话信息")
    @GetMapping("/info.single")
    fun findSingle(
            @RequestParam
            @ApiParam(value = "发送人消息账号")
            sender: String,
            @RequestParam
            @ApiParam(value = "接收人消息账号")
            receiver: String
    ): BaseResult

    @ApiOperation(value = "获取群聊会话群信息")
    @GetMapping("/info.group")
    fun findGroup(
            @RequestParam
            @ApiParam(value = "群聊ID")
            groupId: Long
    ): BaseResult

}