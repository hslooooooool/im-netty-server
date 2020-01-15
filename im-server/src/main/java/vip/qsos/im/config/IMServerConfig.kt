package vip.qsos.im.config

import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import vip.qsos.im.handler.BindAccountRequestHandler
import vip.qsos.im.handler.SessionCloseRequestHandler
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.handler.IMServerInboundHandler
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.SessionClientBo
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Configuration
open class IMServerConfig constructor(
        @Resource private val mProperties: AppProperties,
        @Resource private val mApplicationContext: ApplicationContext
) : IMRequestHandler, ApplicationListener<ApplicationStartedEvent> {

    private val mAppHandlerMap = HashMap<String, Class<out IMRequestHandler>>()

    @PostConstruct
    private fun initHandler() {
        /**账号绑定handler*/
        mAppHandlerMap[IMConstant.CLIENT_BIND] = BindAccountRequestHandler::class.java
        /**连接关闭handler*/
        mAppHandlerMap[IMConstant.CLIENT_CLOSED] = SessionCloseRequestHandler::class.java
    }

    @Bean(destroyMethod = "destroy")
    open fun getIMSocketAcceptor(): IMServerInboundHandler {
        return IMServerInboundHandler().build(IMServerInboundHandler.Builder(
                mAppHandler = this,
                mPort = mProperties.port,
                mHeartbeatPingTime = 30,
                mReadIdleTime = 150,
                mWriteIdleTime = 150
        ))
    }

    override fun onApplicationEvent(applicationStartedEvent: ApplicationStartedEvent) {
        mApplicationContext.getBean(IMServerInboundHandler::class.java).start()
    }

    override fun process(sessionClient: SessionClientBo, message: SendBody) {
        mAppHandlerMap[message.key]?.let {
            mApplicationContext.getBean(it).process(sessionClient, message)
        }
    }
}