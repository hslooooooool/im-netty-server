package vip.qsos.im.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.*
import vip.qsos.im.model.BaseResult

@Api(tags = ["消息群管理"])
@ApiSort(3)
@RequestMapping("/api/im/group")
interface GroupApi {

    @ApiOperation(value = "群ID获取群信息")
    @GetMapping("/info.id")
    fun findByGroupId(
            @RequestParam
            @ApiParam(value = "群ID")
            groupId: String
    ): BaseResult

    @ApiOperation(value = "群名称获取群信息")
    @GetMapping("/info.name")
    fun findByName(
            @RequestParam
            @ApiParam(value = "群名称")
            name: String,
            @RequestParam
            @ApiParam(value = "是否模糊查询", required = false, defaultValue = "false")
            like: Boolean = false
    ): BaseResult

    @ApiOperation(value = "获取单聊关系的群信息", notes = "存在，则表面两人为好友关系，否则不是好友关系")
    @GetMapping("/info.single")
    fun findSingle(
            @RequestParam
            @ApiParam(value = "发送人消息账号")
            sender: String,
            @RequestParam
            @ApiParam(value = "接收人消息账号")
            receiver: String
    ): BaseResult

    @ApiOperation(value = "获取群聊关系的群信息")
    @GetMapping("/info.group")
    fun findGroup(
            @RequestParam
            @ApiParam(value = "群ID")
            groupId: String
    ): BaseResult

    @ApiOperation(value = "创建群")
    @PostMapping("/create")
    fun create(
            @RequestParam
            @ApiParam(value = "群名称")
            name: String,
            @RequestParam
            @ApiParam(value = "创建人消息账号")
            creator: String,
            @RequestParam("members")
            @ApiParam(value = "群成员账号集合")
            memberList: List<String>
    ): BaseResult

    @ApiOperation(value = "群列表")
    @GetMapping("/list")
    fun list(): BaseResult

    @ApiOperation(value = "删除群")
    @DeleteMapping("/")
    fun deleteGroup(
            @RequestParam
            @ApiParam(value = "群ID")
            groupId: String
    ): BaseResult

    @ApiOperation(value = "加入群")
    @PostMapping("/join")
    fun joinGroup(
            @RequestParam
            @ApiParam(value = "群ID")
            groupId: String,
            @RequestParam
            @ApiParam(value = "加入的成员账号")
            member: String
    ): BaseResult

    @ApiOperation(value = "离开群")
    @PostMapping("/leave")
    fun leaveGroup(
            @RequestParam
            @ApiParam(value = "群ID")
            groupId: String,
            @RequestParam
            @ApiParam(value = "离开的成员账号")
            member: String
    ): BaseResult
}