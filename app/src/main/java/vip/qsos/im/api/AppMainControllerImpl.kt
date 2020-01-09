package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.UserManageComponent
import vip.qsos.im.model.AppUser
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.ChatSessionBo
import vip.qsos.im.model.db.TableChatMessageOfGroup
import vip.qsos.im.repository.db.*
import vip.qsos.im.service.FriendService
import javax.annotation.Resource

@RestController
class AppMainControllerImpl : AppMainApi {
    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository
    @Resource
    private lateinit var mGroupRepository: TableChatGroupRepository
    @Resource
    private lateinit var mGroupInfoRepository: TableChatGroupInfoRepository
    @Resource
    private lateinit var mMessageOfGroupRepository: TableChatMessageOfGroupRepository
    @Resource
    private lateinit var mFriendService: FriendService
    @Resource
    private lateinit var mUserManageComponent: UserManageComponent

    override fun sessionList(userId: Long): BaseResult {

        val sessions = mSessionRepository.findAll().map { session ->
            // TODO 待修改对应用户的会话列表,判断 SessionType
            mGroupRepository.findBySessionId(session.sessionId)!!.let { group ->
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
        }
        return BaseResult.data(sessions)
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