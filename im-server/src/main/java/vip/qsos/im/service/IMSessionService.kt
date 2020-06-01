package vip.qsos.im.service

import vip.qsos.im.lib.server.model.IMSession

/**消息连接管理接口
 * @author : 华清松
 */
interface IMSessionService {
    /**保存连接信息*/
    fun save(sessionClient: IMSession)

    /**查询消息账号对应的连接信息
     * @param account 消息账号*/
    fun find(account: String): IMSession?

    /**删除消息账号对应的连接信息*/
    fun remove(account: String)

    /**下线消息账号对应的连接信息*/
    fun offline(account: String)

    /**获取连接信息列表*/
    fun list(): List<IMSession>
}