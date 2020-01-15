package vip.qsos.im.dispense

import vip.qsos.im.model.AppException
import vip.qsos.im.model.AppUserBo
import vip.qsos.im.model.LoginUser
import vip.qsos.im.model.db.TableFriend
import vip.qsos.im.model.db.TableUser
import javax.transaction.Transactional

/**
 * @author : 华清松
 * 用户管理
 */
@Transactional
interface UserManager {

    fun register(name: String, password: String): LoginUser
    fun login(name: String, password: String): LoginUser

    fun findAll(): List<AppUserBo>
    fun findByName(name: String): AppUserBo?
    fun findById(userId: Long): AppUserBo
    fun findMine(userId: Long): AppUserBo
    fun findByNameLike(name: String): List<AppUserBo>
    fun findByImAccount(account: String): AppUserBo?
    /**分配账号*/
    @Throws(AppException::class)
    fun assignImAccount(user: TableUser): TableUser

    /**添加好友*/
    fun addFriend(userId: Long, friendId: Long): TableFriend

}