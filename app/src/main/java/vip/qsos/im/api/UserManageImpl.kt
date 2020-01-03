package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.UserManageComponent
import vip.qsos.im.model.BaseResult
import javax.annotation.Resource

@RestController
class UserManageImpl : UserApi {

    @Resource
    private lateinit var mUserManageComponent: UserManageComponent

    override fun register(account: String, password: String): BaseResult {
        val user = mUserManageComponent.register(account, password)
        return BaseResult.data(user)
    }

    override fun login(account: String, password: String): BaseResult {
        val user = mUserManageComponent.login(account, password)
        return BaseResult.data(user)
    }

    override fun list(): BaseResult {
        val user = mUserManageComponent.findAll()
        return BaseResult.data(user)
    }

    override fun findByUserId(userId: Long): BaseResult {
        val user = mUserManageComponent.findById(userId)
        return BaseResult.data(user)
    }

    override fun addFriend(userId: Long, friendId: Long): BaseResult {
        val sender = mUserManageComponent.findById(userId)
        val receiver = mUserManageComponent.findById(friendId)
        val friend = mUserManageComponent.addFriend(sender, receiver)
        return BaseResult.data(friend)
    }

    override fun findFriend(userId: Long, friendId: Long): BaseResult {
        val friend = mUserManageComponent.findFriend(userId, friendId)
        return BaseResult.data(friend)
    }
}