package vip.qsos.im.service

import cn.teaey.apns4j.Apns4j
import cn.teaey.apns4j.network.ApnsGateway
import org.springframework.stereotype.Service
import vip.qsos.im.config.AppProperties
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.ApnsPayloadCompat
import javax.annotation.Resource

@Service
class ApnsPusher constructor(
        @Resource private val mProperties: AppProperties
) : IApnsPusher {
    override fun push(message: Message, deviceToken: String) {
        mProperties.apnsP12File?.let { path ->
            val stream = javaClass.getResourceAsStream(path)
            val channel = Apns4j.newChannelFactoryBuilder()
                    .keyStoreMeta(stream)
                    .keyStorePwd(mProperties.apnsP12Password)
                    .apnsGateway(
                            if (mProperties.apnsDebug) {
                                ApnsGateway.DEVELOPMENT
                            } else {
                                ApnsGateway.PRODUCTION
                            }
                    )
                    .build()
            val apnsChannel = channel.newChannel()
            try {
                val apnsPayload = ApnsPayloadCompat().also {
                    it.action = message.action
                    it.content = message.content
                    it.sender = message.sender
                    it.format = message.format
                    it.receiver = message.receiver
                }
                apnsChannel.send(deviceToken, apnsPayload)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                apnsChannel.close()
                stream.close()
            }
        } ?: throw NullPointerException("苹果证书不存在")
    }
}