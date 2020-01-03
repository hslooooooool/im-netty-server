package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.form.SendMessageForm
import vip.qsos.im.model.form.SendNoticeForm

@Api(tags = ["消息发送"])
@ApiSort(1)
@RequestMapping("/api/im/send")
interface MessageSendApi {
    @ApiOperation(value = "发送消息")
    @PostMapping("/message")
    fun send(
            @RequestBody
            message: SendMessageForm
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