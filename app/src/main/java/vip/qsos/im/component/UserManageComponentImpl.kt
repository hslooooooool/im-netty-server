package vip.qsos.im.component

import org.springframework.stereotype.Component
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

    override fun findByName(name: String): TableUser? {
        return mUserManageService.findByName(name)
    }

    override fun findByNameLike(name: String): List<TableUser> {
        return mUserManageService.findByNameLike(name)
    }

    override fun findByImAccount(account: String): TableUser? {
        return mUserManageService.findByImAccount(account)
    }

}