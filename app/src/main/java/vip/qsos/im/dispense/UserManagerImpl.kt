package vip.qsos.im.dispense

import org.springframework.stereotype.Component
import vip.qsos.im.model.AppUserBo
import vip.qsos.im.model.LoginUser
import vip.qsos.im.model.db.TableFriend
import vip.qsos.im.model.db.TableUser
import vip.qsos.im.service.IMAccountService
import vip.qsos.im.service.FriendService
import vip.qsos.im.service.UserService
import javax.annotation.Resource

/**
 * @author : 华清松
 */
@Component
class UserManagerImpl : UserManager {

    @Resource
    private lateinit var mUserService: UserService
    @Resource
    private lateinit var mFriendService: FriendService
    @Resource
    private lateinit var mIMAccountService: IMAccountService

    override fun assignImAccount(user: TableUser): TableUser {
        val account = mIMAccountService.assign()
        assert(account.length == 9)
        user.imAccount = account
        return mUserService.updateUser(user)
    }

    override fun register(name: String, password: String): LoginUser {
        return assignImAccount(mUserService.register(name, password)).let {
            LoginUser.getBo(it)
        }
    }

    override fun login(name: String, password: String): LoginUser {
        return mUserService.login(name, password).let {
            LoginUser.getBo(it)
        }
    }

    override fun findById(userId: Long): AppUserBo {
        return mUserService.findById(userId).let {
            AppUserBo(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findMine(userId: Long): AppUserBo {
        return mUserService.findById(userId).let {
            AppUserBo(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findAll(): List<AppUserBo> {
        return mUserService.findAll().map {
            AppUserBo(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findByName(name: String): AppUserBo? {
        return mUserService.findByName(name)?.let {
            AppUserBo(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findByNameLike(name: String): List<AppUserBo> {
        return mUserService.findByNameLike(name).map {
            AppUserBo(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findByImAccount(account: String): AppUserBo? {
        return mUserService.findByImAccount(account)?.let {
            AppUserBo(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun addFriend(userId: Long, friendId: Long): TableFriend {
        val friend = mFriendService.addFriend(userId, friendId)
        // TODO 发送申请消息
        return friend
    }

}