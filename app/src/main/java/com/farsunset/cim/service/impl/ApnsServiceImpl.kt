package com.farsunset.cim.service.impl

import cn.teaey.apns4j.Apns4j
import cn.teaey.apns4j.network.ApnsGateway
import com.farsunset.cim.service.ApnsService
import com.farsunset.cim.util.ApnsPayloadCompat
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.model.Message

@Service
class ApnsServiceImpl : ApnsService {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(ApnsServiceImpl::class.java)
    }

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
        val apnsChannelFactory = Apns4j.newChannelFactoryBuilder()
                .keyStoreMeta(stream)
                .keyStorePwd(password)
                .apnsGateway(if (isDebug) ApnsGateway.DEVELOPMENT else ApnsGateway.PRODUCTION)
                .build()
        val apnsChannel = apnsChannelFactory.newChannel()
        try {
            val apnsPayload = ApnsPayloadCompat()
            apnsPayload.action = message!!.action
            apnsPayload.content = message.content
            apnsPayload.sender = message.sender
            apnsPayload.format = message.format
            apnsPayload.receiver = message.receiver
            apnsChannel.send(deviceToken, apnsPayload)
            LOGGER.info("$deviceToken\r\ndata: {}", apnsPayload.toJsonString())
        } catch (exception: Exception) {
            LOGGER.error("Apns has error", exception)
        } finally {
            apnsChannel.close()
            IOUtils.closeQuietly(stream)
        }
    }

}