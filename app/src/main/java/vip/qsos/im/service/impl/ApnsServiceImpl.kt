package vip.qsos.im.service.impl

import cn.teaey.apns4j.Apns4j
import cn.teaey.apns4j.network.ApnsGateway
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.service.ApnsService
import vip.qsos.im.util.ApnsPayloadCompat

@Service
class ApnsServiceImpl : ApnsService {

    @Value("\${apple.apns.p12.password}")
    private var password: String? = null
    @Value("\${apple.apns.p12.file}")
    private var p12Path: String = ""
    @Value("\${apple.apns.debug}")
    private var isDebug = false

    override fun push(message: Message?, deviceToken: String?) {
        if (StringUtils.isBlank(deviceToken)) {
            return
        }
        val stream = javaClass.getResourceAsStream(p12Path)
        val channel = Apns4j.newChannelFactoryBuilder()
                .keyStoreMeta(stream)
                .keyStorePwd(password)
                .apnsGateway(if (isDebug) ApnsGateway.DEVELOPMENT else ApnsGateway.PRODUCTION)
                .build()
        val apnsChannel = channel.newChannel()
        try {
            val apnsPayload = ApnsPayloadCompat().also {
                it.action = message!!.action
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
    }

}