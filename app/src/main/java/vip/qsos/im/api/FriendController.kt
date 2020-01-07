package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.UserManageComponent
import vip.qsos.im.model.BaseResult
import vip.qsos.im.service.ChatGroupRepository
import vip.qsos.im.service.FriendService
import vip.qsos.im.service.UserService
import javax.annotation.Resource

@RestController
class FriendController : FriendApi {

    @Resource
    private lateinit var mFriendService: FriendService
    @Resource
    private lateinit var mUserService: UserService
    @Resource
    private lateinit var mUserManageComponent: UserManageComponent
    @Resource
    private lateinit var mChatGroupRepository: ChatGroupRepository

    override fun add(userId: Long, friendId: Long): BaseResult {
        val friend = mUserManageComponent.addFriend(userId, friendId)
        return BaseResult.data(friend)
    }

    override fun find(userId: Long, friendId: Long): BaseResult {
        val friend = mFriendService.findFriend(userId, friendId)
        return BaseResult.data(friend)
    }

    override fun findByFriendAndAccept(friendId: Long, accept: Boolean?): BaseResult {
        val friends = mFriendService.findByFriendAndAccept(friendId, accept)
        return BaseResult.data(friends)
    }

    override fun accept(id: Long, accept: Boolean): BaseResult {
        val friend = mFriendService.updateFriend(id, accept)
        if (accept) {
            /**创建会话与单聊信息*/
            val imAccount1 = mUserService.findById(friend.applicant).imAccount
            val imAccount2 = mUserService.findById(friend.friend).imAccount
            mChatGroupRepository.create("单聊$id", imAccount1, arrayListOf(imAccount1, imAccount2))
        }
        return BaseResult.data(friend)
    }
}