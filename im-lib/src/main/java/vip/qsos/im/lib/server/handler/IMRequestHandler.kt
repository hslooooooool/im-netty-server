package vip.qsos.im.lib.server.handler

import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.lib.server.model.IMSendBody
import vip.qsos.im.lib.server.model.IMSession

/**
 * @author : 华清松
 * 收到消息处理接口
 */
interface IMRequestHandler {
    /**消息处理
     * @param sessionClient 当前连接通道
     * @param message 发送的消息
     * */
    @Throws(IMException::class)
    fun process(sessionClient: IMSession, message: IMSendBody)
}