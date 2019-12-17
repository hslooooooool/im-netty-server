package com.farsunset.cim.push

import com.farsunset.cim.service.ApnsService
import com.farsunset.cim.service.IMSessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.model.Message
import javax.annotation.Resource

/**
 * @author : 华清松
 * 消息发送实现类
 */
@Component
class MessagePusher : IMMessagePusher {
    @Value("\${im.server.host.ip}")
    private val host: String? = null
    @Resource
    private val imSessionService: IMSessionService? = null
    @Resource
    private val apnsService: ApnsService? = null

    override fun push(msg: Message) {
        val session = imSessionService!!.find(msg.receiver) ?: return
        /**IOS设备，如果开启了apns，则使用apns推送*/
        if (session.isIOSChannel && session.isApnsOpen) {
            apnsService!!.push(msg, session.deviceId)
            return
        }
        /** 服务器集群时，判断当前session是否连接于本台服务器，如果连接到了其他服务器则转发请求到目标服务器*/
        if (session.isConnected && host != session.host) {
            /**TODO 在此调用目标服务器接口来发送*/
            return
        }
        /**如果是Android，浏览器或者windows客户端则直接发送 */
        if (session.isConnected && host == session.host) {
            session.write(msg)
        }
    }
}