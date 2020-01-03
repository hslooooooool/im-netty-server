package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.*
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.form.SendNoticeForm
import javax.validation.constraints.NotNull

@Api(tags = ["消息发送"])
@ApiSort(1)
@RequestMapping("/api/im/send")
interface MessageSendApi {
    @ApiOperation(value = "发送消息")
    @PostMapping("/message")
    fun send(
            @RequestParam
            @ApiParam(value = "消息类型，如0:文本、1:文件等，默认：0")
            @NotNull(message = "消息类型不能为空")
            action: String = "0",
            @RequestParam
            @ApiParam(value = "消息类型")
            @NotNull(message = "消息类型不能为空")
            contentType: Int,
            @RequestParam
            @ApiParam(value = "消息内容")
            @NotNull(message = "消息内容不能为空")
            content: String,
            @RequestParam
            @ApiParam(value = "消息发送者账号")
            @NotNull(message = "发送账号不能为空")
            sender: String,
            @RequestParam
            @ApiParam(value = "消息接收群ID")
            @NotNull(message = "接收账号不能为空")
            groupId: String
    ): BaseResult

    @ApiOperation(value = "发送通知")
    @PostMapping("/notice")
    fun send(
            @RequestBody
            notice: SendNoticeForm
    ): BaseResult

}

@Api(tags = ["消息管理"])
@ApiSort(2)
@RequestMapping("/api/im/message")
interface MessageMangeApi {

    @ApiOperation(value = "消息列表")
    @GetMapping("/list")
    fun list(): BaseResult

}