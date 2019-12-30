package vip.qsos.im.repository

import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.db.TableChatAccount
import vip.qsos.im.repository.db.IChatAccountRepository
import javax.annotation.Resource

@Repository
class AccountRepository constructor(
        @Resource private val mAccountRepository: IChatAccountRepository
) : IAccountRepository {

    override fun assign(): String {
        val account = mAccountRepository.findTopByUsed(false)
                ?: throw ImException("无可用账号")
        account.used = true
        mAccountRepository.save(account)
        return account.account
    }

    override fun findByAccount(account: String): TableChatAccount? {
        return mAccountRepository.findByAccount(account)
    }

    override fun list(used: Boolean?): List<TableChatAccount> {
        return used?.let {
            mAccountRepository.findAllByUsed(used)
        } ?: mAccountRepository.findAll()
    }

}