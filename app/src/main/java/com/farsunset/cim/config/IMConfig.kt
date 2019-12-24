package com.farsunset.cim.config

import com.farsunset.cim.handler.BindAccountRequestHandler
import com.farsunset.cim.handler.SessionCloseRequestHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import vip.qsos.im.lib.server.constant.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.handler.IMSocketAcceptor
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.Session
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Configuration
open class IMConfig : IMRequestHandler, ApplicationListener<ApplicationStartedEvent> {
    @Resource
    private val applicationContext: ApplicationContext? = null
    private val appHandlerMap = HashMap<String, Class<out IMRequestHandler>>()

    @PostConstruct
    private fun initHandler() {
        /**账号绑定handler*/
        appHandlerMap[IMConstant.CLIENT_CONNECT_BIND] = BindAccountRequestHandler::class.java
        /**连接关闭handler*/
        appHandlerMap[IMConstant.CLIENT_CONNECT_CLOSED] = SessionCloseRequestHandler::class.java
    }

    @Bean(destroyMethod = "destroy")
    open fun getIMSocketAcceptor(@Value("\${im.server.port}") port: Int): IMSocketAcceptor {
        val nioSocketAcceptor = IMSocketAcceptor()
        nioSocketAcceptor.setPort(port)
        nioSocketAcceptor.setAppHandler(this)
        return nioSocketAcceptor
    }

    override fun process(session: Session?, message: SendBody?) {
        findHandlerByKey(message!!.key)?.process(session, message)
    }

    private fun findHandlerByKey(key: String?): IMRequestHandler? {
        return appHandlerMap[key]?.let {
            applicationContext!!.getBean(it)
        }
    }

    /** spring boot 启动完成之后再启动im服务，避免服务正在重启时，客户端会立即开始连接导致意外异常发生*/
    override fun onApplicationEvent(applicationStartedEvent: ApplicationStartedEvent) {
        applicationContext!!.getBean(IMSocketAcceptor::class.java).bind()
    }
}