package vip.qsos.im.component

import org.springframework.stereotype.Component
import vip.qsos.im.model.AppUser
import vip.qsos.im.model.db.TableFriend
import vip.qsos.im.model.db.TableUser
import vip.qsos.im.service.ChatAccountRepository
import vip.qsos.im.service.ChatGroupRepository
import vip.qsos.im.service.FriendService
import vip.qsos.im.service.UserService
import javax.annotation.Resource

/**
 * @author : 华清松
 * 用户管理
 */
@Component
class UserManageComponentImpl : UserManageComponent {

    @Resource
    private lateinit var mUserService: UserService
    @Resource
    private lateinit var mFriendService: FriendService
    @Resource
    private lateinit var mChatAccountRepository: ChatAccountRepository
    @Resource
    private lateinit var mChatGroupRepository: ChatGroupRepository

    override fun register(name: String, password: String): TableUser {
        return assignImAccount(mUserService.register(name, password))
    }

    override fun assignImAccount(user: TableUser): TableUser {
        val account = mChatAccountRepository.assign()
        assert(account.length == 9)
        user.imAccount = account
        return mUserService.updateUser(user)
    }

    override fun login(name: String, password: String): TableUser {
        return mUserService.login(name, password)
    }

    override fun findById(userId: Long): AppUser {
        return mUserService.findById(userId).let {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findMine(userId: Long): TableUser {
        return mUserService.findById(userId)
    }

    override fun findAll(): List<AppUser> {
        return mUserService.findAll().map {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findByName(name: String): AppUser? {
        return mUserService.findByName(name)?.let {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findByNameLike(name: String): List<AppUser> {
        return mUserService.findByNameLike(name).map {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findByImAccount(account: String): AppUser? {
        return mUserService.findByImAccount(account)?.let {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun addFriend(userId: Long, friendId: Long): TableFriend {
        val friend = mFriendService.addFriend(userId, friendId)
        // TODO 发送申请消息
        return friend
    }

}