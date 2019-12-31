package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.model.BaseResult
import vip.qsos.im.repository.ISessionGroupRepository
import javax.annotation.Resource

@RestController
class GroupControllerImpl : GroupApi {
    @Resource
    private lateinit var mGroupRepository: ISessionGroupRepository

    override fun findByGroupId(groupId: Int): BaseResult {
        return BaseResult.data(mGroupRepository.findByGroupId(groupId))
    }

    override fun findByName(name: String, like: Boolean): BaseResult {
        return BaseResult.data(mGroupRepository.findByName(name, like))
    }

    override fun create(name: String, memberList: List<String>): BaseResult {
        return BaseResult.data(mGroupRepository.create(name, memberList))
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