package vip.qsos.im.service

import vip.qsos.im.lib.server.model.IMMessage

/**
 * @author : 华清松
 * 苹果推送服务接口
 */
interface IMApnsService {
    /**推送消息*/
    fun push(message: IMMessage, deviceToken: String)
}