package vip.qsos.im.component

import org.springframework.stereotype.Component
import vip.qsos.im.model.AppUser
import vip.qsos.im.model.db.TableUser
import vip.qsos.im.repository.IAccountRepository
import vip.qsos.im.service.UserManageService
import javax.annotation.Resource

/**
 * @author : 华清松
 * 用户管理
 */
@Component
class UserManageComponentImpl : UserManageComponent {

    @Resource
    private lateinit var mUserManageService: UserManageService
    @Resource
    private lateinit var mAccountRepository: IAccountRepository

    override fun register(name: String, password: String): TableUser {
        return assignImAccount(mUserManageService.register(name, password))
    }

    override fun assignImAccount(user: TableUser): TableUser {
        val account = mAccountRepository.assign()
        assert(account.length == 9)
        user.imAccount = account
        return mUserManageService.updateUser(user)
    }

    override fun login(name: String, password: String): TableUser {
        return mUserManageService.login(name, password)
    }

    override fun findById(userId: Long): AppUser {
        return mUserManageService.findById(userId).let {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findMine(userId: Long): TableUser {
        return mUserManageService.findById(userId)
    }

    override fun findAll(): List<AppUser> {
        return mUserManageService.findAll().map {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findByName(name: String): AppUser? {
        return mUserManageService.findByName(name)?.let {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findByNameLike(name: String): List<AppUser> {
        return mUserManageService.findByNameLike(name).map {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

    override fun findByImAccount(account: String): AppUser? {
        return mUserManageService.findByImAccount(account)?.let {
            AppUser(it.userId, it.name, it.imAccount, it.avatar)
        }
    }

}