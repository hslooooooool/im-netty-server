package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.form.SendMessageForm
import vip.qsos.im.model.form.SendNoticeForm

@Api(tags = ["消息服务"])
@ApiSort(2)
@RequestMapping("/api/im.message")
interface IMessageApi {
    @ApiOperation(value = "分布式消息发送")
    @PostMapping("/dispatch")
    fun dispatch(
            @RequestBody
            message: SendMessageForm
    ): BaseResult

    @ApiOperation(value = "分布式公告发送")
    @PostMapping("/dispatch")
    fun dispatch(
            @RequestBody
            notice: SendNoticeForm
    ): BaseResult

    @ApiOperation(value = "发送消息")
    @PostMapping("/send")
    fun send(
            @RequestBody
            message: SendMessageForm
    ): BaseResult

    @ApiOperation(value = "发送公告")
    @PostMapping("/notice")
    fun send(
            @RequestBody
            notice: SendNoticeForm
    ): BaseResult
}