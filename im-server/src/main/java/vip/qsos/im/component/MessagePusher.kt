package vip.qsos.im.component

import org.springframework.stereotype.Component
import vip.qsos.im.config.AppProperties
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.service.ApnsPusher
import vip.qsos.im.service.SessionClientManager
import javax.annotation.Resource

/**
 * @author : 华清松
 * 消息发送实现类
 */
@Component
class MessagePusher constructor(
        @Resource private val mProperties: AppProperties,
        @Resource private val mSessionClientManager: SessionClientManager,
        @Resource private val mApnsPusher: ApnsPusher
) : IMessagePusher {
    override fun push(msg: Message) {
        //TODO 设计发送失败的处理机制
        mSessionClientManager.find(msg.receiver)?.let { session ->
            when {
                session.isIOSChannel && session.isApnsOpen -> {
                    /**IOS设备，如果开启了apns，则使用apns推送*/
                    session.deviceId?.let { token ->
                        mApnsPusher.push(msg, token)
                    } ?: throw ImException("消息发送失败，苹果DEVICE TOKEN不存在")
                }
                session.isConnected && mProperties.hostIp != session.host -> {
                    /**通道正常，但连接的是其它服务器，转交发送*/
                    /**TODO 在此调用目标服务器接口来发送*/
                }
                session.isConnected && mProperties.hostIp == session.host -> {
                    /**通道正常，连接的是当前服务器，直接发送*/
                    msg.format = Message.Format.PROTOBUF.name
                    session.write(msg)
                }
                else -> {
                    throw ImException("消息发送失败，消息通道已关闭")
                }
            }
        } ?: throw ImException("消息发送失败，接收账号未上线")
    }
}