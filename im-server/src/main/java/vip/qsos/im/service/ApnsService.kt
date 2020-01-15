package vip.qsos.im.service

import vip.qsos.im.lib.server.model.Message

/**
 * @author : 华清松
 * 苹果推送服务接口
 */
interface ApnsService {
    /**推送消息*/
    fun push(message: Message, deviceToken: String)
}