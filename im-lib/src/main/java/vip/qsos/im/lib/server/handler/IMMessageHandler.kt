package vip.qsos.im.lib.server.handler

import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.lib.server.model.IMMessage
import vip.qsos.im.lib.server.model.IMSession

/**收到 IMMessage 消息处理接口
 * @author : 华清松
 */
interface IMMessageHandler {
    /**消息处理
     * @param sessionClient 当前连接通道
     * @param message 消息
     * */
    @Throws(IMException::class)
    fun process(sessionClient: IMSession, message: IMMessage)
}