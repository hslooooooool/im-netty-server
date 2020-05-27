package vip.qsos.im.config

import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import vip.qsos.im.handler.NullHandler
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.handler.IMServerInboundHandler
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.SessionClientBo
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.Resource

abstract class IMServerConfig : IMRequestHandler, ApplicationListener<ApplicationStartedEvent> {
    @Resource
    lateinit var mApplicationContext: ApplicationContext

    @Resource
    lateinit var mProperties: AppProperties

    private val mAppHandlerMap = HashMap<String, Class<out IMRequestHandler>>()

    /**添加消息处理器*/
    abstract fun addHandler(handlerMap: HashMap<String, Class<out IMRequestHandler>>)

    @PostConstruct
    private fun initHandler() {
        /**无处理器handler*/
        mAppHandlerMap[IMConstant.CLIENT_NULL_HANDLER] = NullHandler::class.java
        addHandler(mAppHandlerMap)
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
        } ?: mAppHandlerMap[IMConstant.CLIENT_NULL_HANDLER]?.let {
            mApplicationContext.getBean(it).process(sessionClient, message)
        } ?: throw ImException("无法处理[${message.key}]消息类型")
    }

}