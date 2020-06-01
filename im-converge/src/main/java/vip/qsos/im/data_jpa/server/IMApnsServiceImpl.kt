package vip.qsos.im.data_jpa.server

import cn.teaey.apns4j.Apns4j
import cn.teaey.apns4j.network.ApnsGateway
import org.springframework.stereotype.Service
import vip.qsos.im.config.IMProperties
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.lib.server.model.IMMessage
import vip.qsos.im.model.ApnsPayloadCompat
import vip.qsos.im.service.IMApnsService
import javax.annotation.Resource

@Service
class IMApnsServiceImpl : IMApnsService {
    @Resource
    private lateinit var mProperties: IMProperties

    override fun push(message: IMMessage, deviceToken: String) {
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
                val apnsPayload = ApnsPayloadCompat(
                        message.action,
                        message.content,
                        message.sender,
                        message.format,
                        message.receiver
                )
                apnsChannel.send(deviceToken, apnsPayload)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                apnsChannel.close()
                stream.close()
            }
        } ?: throw IMException("消息发送失败，苹果证书不存在")
    }
}