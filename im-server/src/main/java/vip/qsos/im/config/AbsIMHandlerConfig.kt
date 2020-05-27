package vip.qsos.im.config

import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import vip.qsos.im.handler.NullHandler
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.handler.IMServerInboundHandler
import vip.qsos.im.lib.server.handler.WebsocketHandShakeHandler
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.SessionClientBo
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.Resource

abstract class AbsIMHandlerConfig : IMRequestHandler, ApplicationListener<ApplicationStartedEvent> {
    @Resource
    lateinit var mApplicationContext: ApplicationContext

    @Resource
    lateinit var mProperties: IMProperties

    private val mAppHandlerMap = HashMap<String, Class<out IMRequestHandler>>()

    /**添加自定义消息处理器*/
    abstract fun addHandler(handlerMap: HashMap<String, Class<out IMRequestHandler>>)

    /**配置连接断开处理器*/
    abstract fun getClientClosedHandler(): Class<out IMRequestHandler>?

    /**配置账号绑定处理器*/
    abstract fun getAccountBindHandler(): Class<out IMRequestHandler>?

    /**配置心跳消息处理器*/
    abstract fun getHeartbeatHandler(): Class<out IMRequestHandler>?

    /**配置连接在线处理器*/
    abstract fun getClientActiveHandler(): Class<out IMRequestHandler>?

    @PostConstruct
    private fun initHandler() {
        getClientClosedHandler()?.let {
            mAppHandlerMap[IMConstant.CLIENT_CLOSED] = it
        }
        getAccountBindHandler()?.let {
            mAppHandlerMap[IMConstant.CLIENT_BIND] = it
        }
        getHeartbeatHandler()?.let {
            mAppHandlerMap[IMConstant.CLIENT_HEARTBEAT] = it
        }
        getClientActiveHandler()?.let {
            mAppHandlerMap[IMConstant.CLIENT_ACTIVE] = it
        }

        /**处理 websocket 握手请求*/
        mAppHandlerMap[IMConstant.CLIENT_HANDSHAKE] = WebsocketHandShakeHandler::class.java
        /**无处理器handler*/
        mAppHandlerMap[IMConstant.CLIENT_NULL_HANDLER] = NullHandler::class.java
        addHandler(mAppHandlerMap)
    }

    @Bean(destroyMethod = "destroy")
    open fun getIMSocketAcceptor(): IMServerInboundHandler {
        return IMServerInboundHandler().build(IMServerInboundHandler.Builder(
                mHandler = this,
                mPort = mProperties.port,
                mHeartbeatPingTime = 30,
                mReadIdleTime = 150,
                mWriteIdleTime = 150
        ))
    }

    override fun onApplicationEvent(applicationStartedEvent: ApplicationStartedEvent) {
        mApplicationContext.getBean(IMServerInboundHandler::class.java).start()
    }

    /**根据自行添加的处理器进行处理*/
    override fun process(sessionClient: SessionClientBo, message: SendBody) {
        mAppHandlerMap[message.key]?.let {
            mApplicationContext.getBean(it).process(sessionClient, message)
        } ?: mAppHandlerMap[IMConstant.CLIENT_NULL_HANDLER]?.let {
            mApplicationContext.getBean(it).process(sessionClient, message)
        } ?: throw ImException("无法处理[${message.key}]消息类型")
    }

}