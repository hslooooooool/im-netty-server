package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.model.AppUser
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.ChatGroupBo
import vip.qsos.im.model.db.TableChatMessageOfGroup
import vip.qsos.im.repository.db.*
import javax.annotation.Resource

@RestController
class GroupControllerImpl : GroupApi {
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

    override fun list(): BaseResult {
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

}