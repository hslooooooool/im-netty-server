package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.model.BaseResult
import vip.qsos.im.repository.GroupRepository
import javax.annotation.Resource

@RestController
class GroupControllerImpl : GroupApi {
    @Resource
    private lateinit var mGroupRepository: GroupRepository

    override fun findByGroupId(groupId: Int): BaseResult {
        return BaseResult.data(mGroupRepository.findByGroupId(groupId))
    }

    override fun findByName(name: String, like: Boolean): BaseResult {
        return BaseResult.data(mGroupRepository.findByName(name, like))
    }

    override fun findSingle(sender: String, receiver: String): BaseResult {
        val friend = mGroupRepository.findSingle(sender, receiver)
        return BaseResult.data(friend)
    }

    override fun findGroup(groupId: String): BaseResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(name: String, creator: String, memberList: List<String>): BaseResult {
        return BaseResult.data(mGroupRepository.create(name, creator, memberList))
    }

    override fun list(): BaseResult {
        return BaseResult.data(mGroupRepository.list())
    }

    override fun deleteGroup(groupId: Int): BaseResult {
        mGroupRepository.deleteGroup(groupId)
        return BaseResult.data("群已删除")
    }

    override fun joinGroup(groupId: Int, member: String): BaseResult {
        mGroupRepository.joinGroup(groupId, member)
        return BaseResult.data()
    }

    override fun leaveGroup(groupId: Int, member: String): BaseResult {
        mGroupRepository.leaveGroup(groupId, member)
        return BaseResult.data()
    }
}