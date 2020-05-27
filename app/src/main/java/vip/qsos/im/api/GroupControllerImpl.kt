package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.model.BaseResult
import vip.qsos.im.service.ChatGroupService
import javax.annotation.Resource

@RestController
class GroupControllerImpl : GroupApi {
    @Resource
    private lateinit var mChatGroupService: ChatGroupService

    override fun findByGroupId(groupId: String): BaseResult {
        return BaseResult.data(mChatGroupService.findGroup(groupId.toLong()))
    }

    override fun findByName(name: String, like: Boolean): BaseResult {
        return BaseResult.data(mChatGroupService.findByName(name, like))
    }

    override fun create(name: String, creator: String, memberList: List<String>): BaseResult {
        return BaseResult.data(mChatGroupService.create(name, creator, memberList))
    }

    override fun list(): BaseResult {
        return BaseResult.data(mChatGroupService.list())
    }

    override fun deleteGroup(groupId: String): BaseResult {
        mChatGroupService.deleteGroup(groupId.toLong())
        return BaseResult.data("群已删除")
    }

    override fun joinGroup(groupId: String, member: String): BaseResult {
        mChatGroupService.joinGroup(groupId.toLong(), member)
        return BaseResult.data()
    }

    override fun leaveGroup(groupId: String, member: String): BaseResult {
        mChatGroupService.leaveGroup(groupId.toLong(), member)
        return BaseResult.data()
    }
}