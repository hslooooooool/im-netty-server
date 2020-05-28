package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.data_jpa.repository.db.*
import vip.qsos.im.dispense.UserManager
import vip.qsos.im.model.AppUserBo
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.ChatSessionBo
import vip.qsos.im.data_jpa.model.table.TableChatMessageOfGroup
import vip.qsos.im.data_jpa.model.table.TableChatMessageOfSingle
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.service.FriendService
import vip.qsos.im.service.UserService
import javax.annotation.Resource

@RestController
class AppMainControllerImpl : AppMainApi {
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
    @Resource
    private lateinit var mFriendService: FriendService
    @Resource
    private lateinit var mUserManager: UserManager
    @Resource
    private lateinit var mUserService: UserService

    override fun sessionList(userId: Long): BaseResult {
        val sessions = arrayListOf<ChatSessionBo>()
        mSessionRepository.findAll().map { session ->
            when (session.sessionType) {
                EnumSessionType.SINGLE -> {
                    mSingleRepository.findBySessionId(session.sessionId)!!.let { single ->
                        mSingleInfoRepository.findById(single.singleId).get().let { info ->
                            var message: TableChatMessageOfSingle? = null
                            info.lastMessageId?.let { lastMessageId ->
                                mMessageOfSingleRepository.findById(lastMessageId).get().let { msg ->
                                    message = msg
                                }
                            }
                            mUserService.findById(userId).let { user ->
                                sessions.add(ChatSessionBo.single(session, user, message))
                            }
                        }
                    }
                }
                EnumSessionType.GROUP -> {
                    mGroupRepository.findBySessionId(session.sessionId)!!.let { group ->
                        mGroupInfoRepository.findByGroupId(group.groupId).let { info ->
                            var message: TableChatMessageOfGroup? = null
                            info.lastMessageId?.let { lastMessageId ->
                                mMessageOfGroupRepository.findById(lastMessageId).get().let { msg ->
                                    message = msg
                                }
                            }
                            sessions.add(ChatSessionBo.group(session, group, info, message))
                        }
                    }
                }
                else -> {
                    // TODO 其它会话列表
                }
            }
        }
        return BaseResult.data(sessions)
    }

    override fun friendList(userId: Long): BaseResult {
        val friends = arrayListOf<AppUserBo>()
        mFriendService.findFriendList(userId).map {
            if (it.applicant == userId) {
                val user = mUserManager.findById(it.friend)
                friends.add(user)
            } else {
                val user = mUserManager.findById(it.applicant)
                friends.add(user)
            }
        }
        return BaseResult.data(friends)
    }


}