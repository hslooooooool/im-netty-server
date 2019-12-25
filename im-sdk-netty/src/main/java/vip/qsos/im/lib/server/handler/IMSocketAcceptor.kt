package vip.qsos.im.lib.server.handler

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.AttributeKey
import vip.qsos.im.lib.server.constant.IMConstant
import vip.qsos.im.lib.server.filter.SendBodyDecoder
import vip.qsos.im.lib.server.filter.SendBodyEncoder
import vip.qsos.im.lib.server.model.HeartbeatRequest
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.Session
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author : 华清松
 * MessageLite 类型的消息处理，Protobuf 类型的消息将被解析成 MessageLite
 */
@Sharable
class IMSocketAcceptor : SimpleChannelInboundHandler<SendBody>() {
    companion object {
        /**连接空闲时间*/
        const val READ_IDLE_TIME = 150
        /**连接空闲时间*/
        const val WRITE_IDLE_TIME = 120
        /**心跳响应超时时间*/
        const val PING_TIME_OUT = 30
        /**APP回调处理KEY*/
        const val APP_HANDLER = "APP_HANDLER"
    }

    private var mPort = 0
    private val mHandlerMap = HashMap<String, IMRequestHandler>()
    private val mChannelGroup = ConcurrentHashMap<String, Channel>()
    private var mBossGroup: EventLoopGroup? = null
    private var mWorkerGroup: EventLoopGroup? = null

    /**预制 websocket 握手请求的处理*/
    fun bind() {
        mHandlerMap[IMConstant.CLIENT_HEARTBEAT] = HeartbeatHandler()
        mHandlerMap[IMConstant.CLIENT_HANDSHAKE] = WebsocketHandler()

        val bootstrap = ServerBootstrap()
        mBossGroup = NioEventLoopGroup()
        mWorkerGroup = NioEventLoopGroup()
        bootstrap.group(mBossGroup, mWorkerGroup)
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
        bootstrap.channel(NioServerSocketChannel::class.java)
        bootstrap.childHandler(object : ChannelInitializer<SocketChannel>() {
            @Throws(Exception::class)
            public override fun initChannel(ch: SocketChannel) {
                ch.pipeline().addLast(LoggingHandler(LogLevel.TRACE))
                ch.pipeline().addLast(IdleStateHandler(READ_IDLE_TIME, WRITE_IDLE_TIME, 0))

                /**消息处理切面-接收消息解码器*/
                ch.pipeline().addLast("SendBodyDecoder", SendBodyDecoder())
                /**消息处理切面-发送消息编码器*/
                ch.pipeline().addLast("SendBodyEncoder", SendBodyEncoder())

                ch.pipeline().addLast(this@IMSocketAcceptor)
            }
        })
        bootstrap.bind(mPort).syncUninterruptibly().channel().closeFuture().addListener {
            destroy()
        }
    }

    /**销毁服务*/
    private fun destroy() {
        if (mBossGroup != null && !mBossGroup!!.isShuttingDown && !mBossGroup!!.isShutdown) {
            try {
                mBossGroup!!.shutdownGracefully()
            } catch (ignore: Exception) {
            }
            return
        }
        if (mWorkerGroup != null && !mWorkerGroup!!.isShuttingDown && !mWorkerGroup!!.isShutdown) {
            try {
                mWorkerGroup!!.shutdownGracefully()
            } catch (ignore: Exception) {
            }
            return
        }
    }

    /**设置应用层的消息处理器*/
    fun setAppHandler(outerRequestHandler: IMRequestHandler) {
        mHandlerMap[APP_HANDLER] = outerRequestHandler
    }

    /**有消息下达*/
    override fun channelRead0(ctx: ChannelHandlerContext, body: SendBody) {
        val session = Session(ctx.channel())
        mHandlerMap[body.key]?.process(session, body)
        mHandlerMap[APP_HANDLER]?.process(session, body)
    }

    /**检测到通道关闭*/
    override fun channelInactive(ctx: ChannelHandlerContext) {
        mChannelGroup.remove(ctx.channel().id().asShortText())
        val session = Session(ctx.channel())
        val body = SendBody()
        body.key = IMConstant.CLIENT_CLOSED
        mHandlerMap[APP_HANDLER]?.process(session, body)
    }

    /**检测到通道活跃*/
    override fun channelActive(ctx: ChannelHandlerContext) {
        mChannelGroup[ctx.channel().id().asShortText()] = ctx.channel()
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is IdleStateEvent && evt.state() == IdleState.WRITER_IDLE) {
            ctx.channel().attr(AttributeKey.valueOf<Any>(IMConstant.KEY_LAST_HEARTBEAT_TIME)).set(System.currentTimeMillis())
            ctx.channel().writeAndFlush(HeartbeatRequest.instance)
        }
        /**如果心跳请求发出【30】秒内没收到响应，则关闭连接*/
        if (evt is IdleStateEvent && evt.state() == IdleState.READER_IDLE) {
            val lastTime = ctx.channel().attr(AttributeKey.valueOf<Any>(IMConstant.KEY_LAST_HEARTBEAT_TIME)).get() as Long?
            if (lastTime != null && System.currentTimeMillis() - lastTime >= PING_TIME_OUT) {
                ctx.channel().close()
            }
            ctx.channel().attr(AttributeKey.valueOf<Any>(IMConstant.KEY_LAST_HEARTBEAT_TIME)).set(null)
        }
    }

    fun setPort(port: Int) {
        this.mPort = port
    }

    fun getManagedSession(id: String?): Channel? {
        return if (id == null) {
            null
        } else mChannelGroup[id]
    }
}