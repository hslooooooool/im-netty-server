package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import vip.qsos.im.model.BaseResult
import javax.validation.constraints.NotNull

@Api(tags = ["单聊页"])
@ApiSort(4)
@RequestMapping("/api/app/session/single")
interface AppSessionOfSingleApi {

    @ApiOperation(value = "发送消息")
    @PostMapping("/message/send")
    fun sendMessage(
            @RequestParam
            @ApiParam(value = "消息会话ID")
            @NotNull(message = "消息会话ID不能为空") sessionId: Long,
            @RequestParam
            @ApiParam(value = "消息类型")
            @NotNull(message = "消息类型不能为空") contentType: Int,
            @RequestParam
            @ApiParam(value = "消息内容")
            @NotNull(message = "消息内容不能为空") content: String,
            @RequestParam
            @ApiParam(value = "消息发送者账号")
            @NotNull(message = "发送账号不能为空") sender: String
    ): BaseResult

    @ApiOperation(value = "消息列表")
    @GetMapping("/message/list")
    fun getMessageListByTimeline(
            @RequestParam
            @ApiParam(value = "会话ID")
            @NotNull(message = "会话ID不能为空") sessionId: Long,
            @RequestParam
            @ApiParam(value = "消息时序")
            @NotNull(message = "消息时序不能为空") timeline: Long,
            @RequestParam(required = false)
            @ApiParam(value = "消息条数") size: Int = 10,
            @RequestParam(required = false)
            @ApiParam(value = "上一页") previous: Boolean = true
    ): BaseResult

}