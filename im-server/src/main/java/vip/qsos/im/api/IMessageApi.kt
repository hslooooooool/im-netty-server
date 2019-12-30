package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.form.SendMessageForm

@Api(tags = ["消息服务"])
@ApiSort(0)
@RequestMapping("/api/im.message")
interface IMessageApi {
    @ApiOperation(value = "分布式消息发送")
    @PostMapping("/dispatch")
    fun dispatch(
            @RequestBody
            message: SendMessageForm
    ): BaseResult

    @ApiOperation(value = "消息发送")
    @PostMapping("/send")
    fun send(
            @RequestBody
            message: SendMessageForm
    ): BaseResult
}