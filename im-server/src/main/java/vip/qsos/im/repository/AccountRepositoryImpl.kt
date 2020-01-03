package vip.qsos.im.repository

import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.db.TableChatAccount
import vip.qsos.im.repository.db.IChatAccountRepository
import javax.annotation.Resource

@Repository
class AccountRepositoryImpl constructor(
        @Resource private val mAccountRepository: IChatAccountRepository
) : AccountRepository {

    override fun assign(): String {
        val account = mAccountRepository.findTopByUsed(false)
                ?: throw ImException("无可用消息账号")
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

    override fun init(size: Int): List<TableChatAccount> {
        val mAccountList = arrayListOf<String>()
        val accounts = mAccountRepository.findAll()
        if (accounts.isEmpty()) {
            for (i in 1..size) {
                var account = i.toString()
                if (account.length > 9) {
                    throw ImException("账号数已达上限")
                } else {
                    val b = 999999999 - i
                    if (b > 0) {
                        val prefixLength = 9 - account.length
                        for (m in 1..prefixLength) {
                            account = "0$account"
                        }
                        mAccountList.add(account)
                    }
                }
            }
        } else {
            val lastAccount = accounts.last().account
            var lastIndex0 = 0
            for (i in 0..lastAccount.length) {
                val a = lastAccount.substring(i, i + 1)
                if ("0" != a) {
                    lastIndex0 = i
                    break
                }
            }
            val a = lastAccount.substring(lastIndex0).toInt()
            for (i in 1..size) {
                val b = a + i
                if (b > 999999999) {
                    throw ImException("账号数已达上限")
                }
                var account = b.toString()
                val prefixLength = 9 - account.length
                for (m in 1..prefixLength) {
                    account = "0$account"
                }
                mAccountList.add(account)
            }
        }
        return mAccountList.map {
            mAccountRepository.saveAndFlush(TableChatAccount(account = it, used = false))
        }
    }
}