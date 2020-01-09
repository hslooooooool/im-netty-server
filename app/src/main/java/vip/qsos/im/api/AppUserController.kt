package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.UserManageComponent
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.ChatSessionBo
import vip.qsos.im.model.db.TableChatMessageOfGroup
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.repository.db.TableChatGroupInfoRepository
import vip.qsos.im.repository.db.TableChatGroupRepository
import vip.qsos.im.repository.db.TableChatMessageOfGroupRepository
import vip.qsos.im.repository.db.TableChatSessionRepository
import vip.qsos.im.service.ChatGroupRepository
import vip.qsos.im.service.FriendService
import vip.qsos.im.service.UserService
import javax.annotation.Resource

@RestController
class AppUserController : AppUserApi {

    @Resource
    private lateinit var mFriendService: FriendService
    @Resource
    private lateinit var mUserService: UserService
    @Resource
    private lateinit var mUserManageComponent: UserManageComponent
    @Resource
    private lateinit var mChatGroupRepository: ChatGroupRepository
    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository
    @Resource
    private lateinit var mGroupRepository: TableChatGroupRepository
    @Resource
    private lateinit var mGroupInfoRepository: TableChatGroupInfoRepository
    @Resource
    private lateinit var mMessageOfGroupRepository: TableChatMessageOfGroupRepository

    override fun findInfo(userId: Long): BaseResult {
        val user = mUserManageComponent.findById(userId)
        return BaseResult.data(user)
    }

    override fun addFriend(userId: Long, friendId: Long): BaseResult {
        val friend = mUserManageComponent.addFriend(userId, friendId)
        return BaseResult.data(friend)
    }

    override fun findFriend(userId: Long, friendId: Long): BaseResult {
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

    override fun findSingle(sender: String, receiver: String): BaseResult {
        val memberString = TableChatSession.addMember(arrayListOf(sender, receiver))
        val session = mSessionRepository
                .findBySessionTypeAndMember(EnumSessionType.SINGLE, memberString)
                ?: throw ImException("会话不存在")
        val sessionInfo = mGroupRepository.findBySessionId(session.sessionId)!!.let { group ->
            mGroupInfoRepository.findByGroupId(group.groupId).let { info ->
                var message: TableChatMessageOfGroup? = null
                info.lastMessageId?.let { lastMessageId ->
                    mMessageOfGroupRepository.findById(lastMessageId).get().let { msg ->
                        message = msg
                    }
                }
                ChatSessionBo.group(session, group, info, message)
            }
        }
        return BaseResult.data(sessionInfo)
    }
}