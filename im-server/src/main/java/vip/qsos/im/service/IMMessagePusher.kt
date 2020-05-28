package vip.qsos.im.service

import vip.qsos.im.lib.server.model.IMMessage

/**消息发送接口
 * @author : 华清松
 */
interface IMMessagePusher {
    /**发送消息
     * @param msg 消息内容*/
    fun push(msg: IMMessage)
}