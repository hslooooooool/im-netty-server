package vip.qsos.im.lib.server.handler

import vip.qsos.im.lib.server.model.Session
import vip.qsos.im.lib.server.model.SendBody

/**
 * @author : 华清松
 * 收到消息处理接口
 */
interface IMRequestHandler {
    /**消息处理
     * @param session 当前连接通道
     * @param message 发送的消息
     * */
    fun process(session: Session?, message: SendBody?)
}