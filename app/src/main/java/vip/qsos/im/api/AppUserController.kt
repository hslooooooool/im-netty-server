package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.data_jpa.repository.db.*
import vip.qsos.im.dispense.UserManager
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.AppException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.ChatSessionBo
import vip.qsos.im.model.db.TableChatMessageOfSingle
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.repository.db.*
import vip.qsos.im.service.ChatSingleService
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
    private lateinit var mUserManager: UserManager
    @Resource
    private lateinit var mChatSingleService: ChatSingleService
    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository
    @Resource
    private lateinit var mGroupRepository: TableChatGroupRepository
    @Resource
    private lateinit var mSingleRepository: TableChatSingleRepository
    @Resource
    private lateinit var mGroupInfoRepository: TableChatGroupInfoRepository
    @Resource
    private lateinit var mSingleInfoRepository: TableChatSingleInfoRepository
    @Resource
    private lateinit var mMessageOfGroupRepository: TableChatMessageOfGroupRepository
    @Resource
    private lateinit var mMessageOfSingleRepository: TableChatMessageOfSingleRepository

    override fun findInfo(userId: Long): BaseResult {
        val user = mUserManager.findById(userId)
        return BaseResult.data(user)
    }

    override fun addFriend(userId: Long, friendId: Long): BaseResult {
        val friend = mUserManager.addFriend(userId, friendId)
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
            val creator = mUserService.findById(friend.applicant).imAccount
            val receiver = mUserService.findById(friend.friend).imAccount
            mChatSingleService.create(creator, receiver)
        }
        return BaseResult.data(friend)
    }

    override fun findSingle(sender: String, receiver: String): BaseResult {
        mUserService.findByImAccount(receiver)?.let { user ->
            val memberString = TableChatSession.addMember(arrayListOf(sender, receiver))
            val session = mSessionRepository
                    .findBySessionTypeAndMember(EnumSessionType.SINGLE, memberString)
                    ?: throw ImException("会话不存在")
            val sessionInfo = mSingleRepository.findBySessionId(session.sessionId)!!.let { group ->
                mSingleInfoRepository.findById(group.singleId).get().let { info ->
                    var message: TableChatMessageOfSingle? = null
                    info.lastMessageId?.let { lastMessageId ->
                        mMessageOfSingleRepository.findById(lastMessageId).get().let { msg ->
                            message = msg
                        }
                    }
                    ChatSessionBo.single(session, user, message)
                }
            }
            return BaseResult.data(sessionInfo)
        } ?: throw AppException("用户不存在")
    }
}