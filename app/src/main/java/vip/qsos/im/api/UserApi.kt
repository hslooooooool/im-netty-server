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

@Api(tags = ["用户管理"])
@ApiSort(1)
@RequestMapping("/api/app/user")
interface UserApi {

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    fun register(
            @RequestParam
            @ApiParam(value = "账号", required = true)
            account: String,
            @RequestParam
            @ApiParam(value = "密码", required = true)
            password: String
    ): BaseResult

    @ApiOperation(value = "用户登录")
    @GetMapping("/login")
    fun login(
            @RequestParam
            @ApiParam(value = "账号", required = true)
            account: String,
            @RequestParam
            @ApiParam(value = "密码", required = true)
            password: String
    ): BaseResult

    @ApiOperation(value = "用户列表")
    @GetMapping("/list")
    fun list(): BaseResult

    @ApiOperation(value = "ID获取用户")
    @GetMapping("/user.id")
    fun findByUserId(
            @RequestParam
            @ApiParam(value = "用户ID", required = true)
            userId: Long
    ): BaseResult

    @ApiOperation(value = "加好友")
    @PostMapping("/friend.add")
    fun addFriend(
            @RequestParam
            @ApiParam(value = "申请用户ID", required = true)
            userId: Long,
            @RequestParam
            @ApiParam(value = "待加好友ID", required = true)
            friendId: Long
    ): BaseResult

    @ApiOperation(value = "获取好友关系")
    @GetMapping("/friend")
    fun findFriend(
            @RequestParam
            @ApiParam(value = "用户ID", required = true)
            userId: Long,
            @RequestParam
            @ApiParam(value = "好友ID", required = true)
            friendId: Long
    ): BaseResult

}