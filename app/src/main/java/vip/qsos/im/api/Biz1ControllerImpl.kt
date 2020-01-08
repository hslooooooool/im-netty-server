package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.UserManageComponent
import vip.qsos.im.model.AppUser
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.ChatGroupBo
import vip.qsos.im.model.db.TableChatMessageOfGroup
import vip.qsos.im.repository.db.*
import vip.qsos.im.service.FriendService
import javax.annotation.Resource

@RestController
class Biz1ControllerImpl : Biz1Api {
    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository
    @Resource
    private lateinit var mGroupRepository: TableChatGroupRepository
    @Resource
    private lateinit var mGroupInfoRepository: TableChatGroupInfoRepository
    @Resource
    private lateinit var mUserRepository: TableUserRepository
    @Resource
    private lateinit var mMessageOfGroupRepository: TableChatMessageOfGroupRepository
    @Resource
    private lateinit var mFriendService: FriendService
    @Resource
    private lateinit var mUserManageComponent: UserManageComponent

    override fun sessionList(userId: Long): BaseResult {
        // TODO 待修改对应用户的会话列表
        val groups = mSessionRepository.findAll().map { session ->
            mGroupRepository.findBySessionId(session.sessionId)!!.let { group ->
                mGroupInfoRepository.findByGroupId(group.groupId).let { info ->
                    var user: AppUser? = null
                    var message: TableChatMessageOfGroup? = null
                    info.lastMessageId?.let { lastMessageId ->
                        mMessageOfGroupRepository.findById(lastMessageId).get().let { msg ->
                            message = msg
                            mUserRepository.findByImAccount(msg.sender)?.let {
                                user = AppUser(it.userId, it.name, it.imAccount, it.avatar)
                            }
                        }
                    }
                    ChatGroupBo.create(session, group, info, message, user)
                }
            }
        }
        return BaseResult.data(groups)
    }

    override fun friendList(userId: Long): BaseResult {
        val friends = arrayListOf<AppUser>()
        mFriendService.findFriendList(userId).map {
            if (it.applicant == userId) {
                val user = mUserManageComponent.findById(it.friend)
                friends.add(user)
            } else {
                val user = mUserManageComponent.findById(it.applicant)
                friends.add(user)
            }
        }
        return BaseResult.data(friends)
    }

}