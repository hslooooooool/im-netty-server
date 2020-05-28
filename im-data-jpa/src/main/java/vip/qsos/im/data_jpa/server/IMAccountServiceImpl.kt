package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Service
import vip.qsos.im.data_jpa.repository.db.TableIMAccountRepository
import vip.qsos.im.lib.server.model.IMAccount
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.model.db.TableIMAccount
import vip.qsos.im.service.IMAccountService
import javax.annotation.Resource

@Service
class IMAccountServiceImpl : IMAccountService {
    @Resource
    private lateinit var mAccountRepository: TableIMAccountRepository

    override fun assign(): String {
        val account = mAccountRepository.findTopByUsed(false)
                ?: throw IMException("无可用消息账号")
        account.used = true
        mAccountRepository.save(account)
        return account.account
    }

    override fun findById(id: Int): IMAccount {
        return mAccountRepository.findById(id).get()
    }

    override fun findByAccount(account: String): IMAccount? {
        return mAccountRepository.findByAccount(account)
    }

    override fun list(used: Boolean?): List<IMAccount> {
        return used?.let {
            mAccountRepository.findAllByUsed(used)
        } ?: mAccountRepository.findAll()
    }

    override fun create(size: Int): List<IMAccount> {
        val mAccountList = arrayListOf<String>()
        val accounts = mAccountRepository.findAll()
        if (accounts.isEmpty()) {
            for (i in 1..size) {
                var account = i.toString()
                if (account.length > 9) {
                    throw IMException("账号数已达上限")
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
                    throw IMException("账号数已达上限")
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
            mAccountRepository.saveAndFlush(TableIMAccount(account = it, used = false))
        }
    }

}