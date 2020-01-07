package vip.qsos.im.service

import org.springframework.stereotype.Service
import vip.qsos.im.model.AppException
import vip.qsos.im.model.db.TableUser
import vip.qsos.im.repository.db.TableFriendRepository
import vip.qsos.im.repository.db.TableUserRepository
import javax.annotation.Resource

@Service
class UserServiceImpl : UserService {
    @Resource
    private lateinit var mTableUserRepository: TableUserRepository
    @Resource
    private lateinit var mTableFriendRepository: TableFriendRepository

    override fun register(name: String, password: String): TableUser {
        if (null != this.findByName(name)) {
            throw AppException("账号已存在")
        }
        return mTableUserRepository.saveAndFlush(TableUser(name = name, password = password))
    }

    override fun login(name: String, password: String): TableUser {
        val user = this.findByName(name)
        if (user?.password != password) {
            throw AppException("密码错误")
        }
        return user
    }

    override fun updateUser(user: TableUser): TableUser {
        assert(user.userId > 0)
        return mTableUserRepository.saveAndFlush(user)
    }

    override fun findById(userId: Long): TableUser {
        try {
            return mTableUserRepository.findById(userId).get()
        } catch (e: Exception) {
            throw AppException("账号不存在")
        }
    }

    override fun findAll(): List<TableUser> {
        return mTableUserRepository.findAll()
    }

    override fun findByName(name: String): TableUser? {
        return mTableUserRepository.findByName(name)
    }

    override fun findByNameLike(name: String): List<TableUser> {
        return mTableUserRepository.findByNameLike(name)
    }

    override fun findByImAccount(account: String): TableUser? {
        return mTableUserRepository.findByImAccount(account)
    }

}