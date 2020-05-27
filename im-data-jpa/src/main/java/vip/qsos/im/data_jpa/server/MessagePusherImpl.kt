package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Component
import vip.qsos.im.config.IMProperties
import vip.qsos.im.dispense.MessagePusher
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.service.ApnsService
import vip.qsos.im.service.SessionClientService
import javax.annotation.Resource

/**
 * @author : 华清松
 * 消息发送实现类
 */
@Component
class MessagePusherImpl : MessagePusher {
    @Resource
    private lateinit var mProperties: IMProperties
    @Resource
    private lateinit var mSessionClientService: SessionClientService
    @Resource
    private lateinit var mApnsService: ApnsService

    override fun push(msg: Message) {
        //TODO 设计发送失败的处理机制
        mSessionClientService.find(msg.receiver)?.let { session ->
            when {
                session.isIOSChannel && session.isApnsOpen -> {
                    /**IOS设备，如果开启了apns，则使用apns推送*/
                    session.deviceId?.let { token ->
                        mApnsService.push(msg, token)
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
                    throw ImException("消息发送失败，终端未上线或推送机制未识别")
                }
            }
        } ?: throw ImException("消息发送结束，接收账号未上线")
    }
}