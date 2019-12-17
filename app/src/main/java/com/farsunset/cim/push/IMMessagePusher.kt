package com.farsunset.cim.push

import vip.qsos.im.lib.server.model.Message

/**
 * @author : 华清松
 * 消息发送实接口
 */
interface IMMessagePusher {
    /**向用户发送消息 */
    fun push(msg: Message)
}