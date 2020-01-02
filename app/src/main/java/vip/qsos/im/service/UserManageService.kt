package vip.qsos.im.service

import vip.qsos.im.model.db.TableUser

/**
 * @author : 华清松
 * 用户管理服务
 */
interface UserManageService {

    fun register(name: String, password: String): TableUser
    fun login(name: String, password: String): TableUser
    fun updateUser(user: TableUser): TableUser
    fun findAll(): List<TableUser>
    fun findByName(name: String): TableUser?
    fun findById(userId: Long): TableUser
    fun findByNameLike(name: String): List<TableUser>
    fun findByImAccount(account: String): TableUser?

}