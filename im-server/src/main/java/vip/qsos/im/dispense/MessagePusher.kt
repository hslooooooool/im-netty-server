package vip.qsos.im.dispense

import vip.qsos.im.lib.server.model.Message

/**
 * @author : 华清松
 * 消息发送实接口
 */
interface MessagePusher {
    /**发送消息 */
    fun push(msg: Message)
}