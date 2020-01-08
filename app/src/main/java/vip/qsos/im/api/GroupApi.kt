package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import vip.qsos.im.model.BaseResult

@Api(tags = ["消息群查询"])
@ApiSort(3)
@RequestMapping("/api/app/group")
interface GroupApi {

    @ApiOperation(value = "群列表")
    @GetMapping("/list")
    fun list(): BaseResult

}