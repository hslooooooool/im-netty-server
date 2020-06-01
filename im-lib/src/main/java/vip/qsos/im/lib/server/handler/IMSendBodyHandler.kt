package vip.qsos.im.lib.server.handler

import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.lib.server.model.IMSendBody
import vip.qsos.im.lib.server.model.IMSession

/**收到 IMSendBody 消息处理接口
 * @author : 华清松
 */
interface IMSendBodyHandler {
    /**消息处理
     * @param sessionClient 当前连接通道
     * @param body 消息
     * */
    @Throws(IMException::class)
    fun process(sessionClient: IMSession, body: IMSendBody)
}