package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.type.EnumSessionType

@Api(tags = ["消息管理"])
@ApiSort(2)
@RequestMapping("/api/im/message")
interface MessageMangeApi {

    @ApiOperation(value = "消息列表")
    @GetMapping("/list")
    fun list(
            @RequestParam
            @ApiParam(value = "消息类型")
            sessionType: EnumSessionType
    ): BaseResult

}