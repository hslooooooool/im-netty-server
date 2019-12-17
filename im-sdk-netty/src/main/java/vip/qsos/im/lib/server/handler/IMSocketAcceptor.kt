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
import vip.qsos.im.lib.server.filter.ServerMessageDecoder
import vip.qsos.im.lib.server.filter.ServerMessageEncoder
import vip.qsos.im.lib.server.model.HeartbeatRequest
import vip.qsos.im.lib.server.model.IMSession
import vip.qsos.im.lib.server.model.SendBody
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Sharable
class IMSocketAcceptor : SimpleChannelInboundHandler<SendBody>() {
    companion object {
        /**连接空闲时间*/
        const val READ_IDLE_TIME = 150
        /**连接空闲时间*/
        const val WRITE_IDLE_TIME = 120
        /**心跳响应超时时间*/
        const val PING_TIME_OUT = 30
    }

    private val innerHandlerMap = HashMap<String, IMRequestHandler>()
    private var outerRequestHandler: IMRequestHandler? = null
    private val channelGroup = ConcurrentHashMap<String, Channel>()
    private var port = 0
    private var bossGroup: EventLoopGroup? = null
    private var workerGroup: EventLoopGroup? = null

    /**预制 websocket 握手请求的处理*/
    fun bind() {
        innerHandlerMap[IMConstant.CLIENT_WEBSOCKET_HANDSHAKE] = WebsocketHandler()
        innerHandlerMap[IMConstant.CLIENT_HEARTBEAT] = HeartbeatHandler()
        val bootstrap = ServerBootstrap()
        bossGroup = NioEventLoopGroup()
        workerGroup = NioEventLoopGroup()
        bootstrap.group(bossGroup, workerGroup)
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
        bootstrap.channel(NioServerSocketChannel::class.java)
        bootstrap.childHandler(object : ChannelInitializer<SocketChannel>() {
            @Throws(Exception::class)
            public override fun initChannel(ch: SocketChannel) {
                /**消息处理切面-接收消息解码器*/
                ch.pipeline().addLast(ServerMessageDecoder())
                /**消息处理切面-发送消息编码器*/
                ch.pipeline().addLast(ServerMessageEncoder())

                ch.pipeline().addLast(LoggingHandler(LogLevel.INFO))
                ch.pipeline().addLast(IdleStateHandler(READ_IDLE_TIME, WRITE_IDLE_TIME, 0))
                ch.pipeline().addLast(this@IMSocketAcceptor)
            }
        })
        val channelFuture = bootstrap.bind(port).syncUninterruptibly()
        channelFuture.channel().closeFuture().addListener { destroy() }
    }

    /**销毁服务*/
    private fun destroy() {
        if (bossGroup != null && !bossGroup!!.isShuttingDown && !bossGroup!!.isShutdown) {
            try {
                bossGroup!!.shutdownGracefully()
            } catch (ignore: Exception) {
            }
            return
        }
        if (workerGroup != null && !workerGroup!!.isShuttingDown && !workerGroup!!.isShutdown) {
            try {
                workerGroup!!.shutdownGracefully()
            } catch (ignore: Exception) {
            }
            return
        }
    }

    /**设置应用层的消息处理器*/
    fun setOuterRequestHandler(outerRequestHandler: IMRequestHandler?) {
        this.outerRequestHandler = outerRequestHandler
    }

    /**有消息下达*/
    override fun channelRead0(ctx: ChannelHandlerContext, body: SendBody) {
        val session = IMSession(ctx.channel())
        val handler = innerHandlerMap[body.key]
        if (handler != null) {
            handler.process(session, body)
            return
        }
        outerRequestHandler?.process(session, body)
    }

    /**检测到通道关闭*/
    override fun channelInactive(ctx: ChannelHandlerContext) {
        channelGroup.remove(ctx.channel().id().asShortText())
        val session = IMSession(ctx.channel())
        val body = SendBody()
        body.key = IMConstant.CLIENT_CONNECT_CLOSED
        outerRequestHandler?.process(session, body)
    }

    /**检测到通道活跃*/
    override fun channelActive(ctx: ChannelHandlerContext) {
        channelGroup[ctx.channel().id().asShortText()] = ctx.channel()
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is IdleStateEvent && evt.state() == IdleState.WRITER_IDLE) {
            ctx.channel().attr(AttributeKey.valueOf<Any>(IMConstant.KEY_HEARTBEAT)).set(System.currentTimeMillis())
            ctx.channel().writeAndFlush(HeartbeatRequest.instance)
        }
        /**如果心跳请求发出【30】秒内没收到响应，则关闭连接*/
        if (evt is IdleStateEvent && evt.state() == IdleState.READER_IDLE) {
            val lastTime = ctx.channel().attr(AttributeKey.valueOf<Any>(IMConstant.KEY_HEARTBEAT)).get() as Long?
            if (lastTime != null && System.currentTimeMillis() - lastTime >= PING_TIME_OUT) {
                ctx.channel().close()
            }
            ctx.channel().attr(AttributeKey.valueOf<Any>(IMConstant.KEY_HEARTBEAT)).set(null)
        }
    }

    fun setPort(port: Int) {
        this.port = port
    }

    fun getManagedSession(id: String?): Channel? {
        return if (id == null) {
            null
        } else channelGroup[id]
    }
}