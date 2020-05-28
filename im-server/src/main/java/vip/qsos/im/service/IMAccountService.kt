package vip.qsos.im.service

import vip.qsos.im.lib.server.model.IMAccount
import vip.qsos.im.lib.server.model.IMException

/**消息账号管理接口
 * @author : 华清松
 */
interface IMAccountService : AbsService<Int, IMAccount> {
    /**申请一个未使用的账号*/
    fun assign(): String

    /**查询账号是否存在
     * @param account 消息账号*/
    fun findByAccount(account: String): IMAccount?

    /**查看账号列表
     * @param used 账号是否被使用*/
    fun list(used: Boolean?): List<IMAccount>

    /**生成可用账号
     * @param size 生成个数*/
    @Throws(IMException::class)
    fun create(size: Int): List<IMAccount>
}