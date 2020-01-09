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

@Api(tags = ["用户资料页"])
@ApiSort(2)
@RequestMapping("/api/app/user")
interface AppUserApi {

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

    @ApiOperation(value = "获取好友关系信息", notes = "存在则判断是否已接受好友申请，" +
            "否则表名未曾建立好友关系，此时可发起好友申请，建立一条关系数据")
    @GetMapping("/friend.info")
    fun findFriend(
            @RequestParam
            @ApiParam(value = "用户ID", required = true)
            userId: Long,
            @RequestParam
            @ApiParam(value = "好友ID", required = true)
            friendId: Long
    ): BaseResult

    @ApiOperation(value = "获取好友申请列表", notes = "accept：null表示获取待处理好友请求列表，true表示已是好友的列表，false表示已拒绝的好友列表")
    @GetMapping("/friend.list")
    fun findByFriendAndAccept(
            @RequestParam
            @ApiParam(value = "好友ID", required = true)
            friendId: Long,
            @RequestParam
            @ApiParam(value = "是否接受")
            accept: Boolean? = null
    ): BaseResult

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/info")
    fun findInfo(
            @RequestParam
            @ApiParam(value = "用户ID", required = true)
            userId: Long
    ): BaseResult

    @ApiOperation(value = "处理好友关系")
    @GetMapping("/friend.accept")
    fun accept(
            @RequestParam
            @ApiParam(value = "关系ID", required = true)
            id: Long,
            @RequestParam
            @ApiParam(value = "是否接受", required = true)
            accept: Boolean
    ): BaseResult

    @ApiOperation(value = "获取单聊会话信息")
    @GetMapping("/session.single")
    fun findSingle(
            @RequestParam
            @ApiParam(value = "发送人消息账号")
            sender: String,
            @RequestParam
            @ApiParam(value = "接收人消息账号")
            receiver: String
    ): BaseResult

}