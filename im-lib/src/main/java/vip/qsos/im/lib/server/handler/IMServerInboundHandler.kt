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
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.filter.SendBodyDecoder
import vip.qsos.im.lib.server.filter.SendBodyEncoder
import vip.qsos.im.lib.server.model.HeartbeatRequest
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.lib.server.model.IMSendBody
import vip.qsos.im.lib.server.model.IMSession
import java.util.concurrent.ConcurrentHashMap

/**消息服务管理与消息接收处理器
 * @author : 华清松
 */
@Sharable
class IMServerInboundHandler : SimpleChannelInboundHandler<IMSendBody>() {

    private var mPort = 0
    private var mReadIdleTime = 150
    private var mWriteIdleTime = 120
    private var mHeartbeatPingTime = 30

    /**自定义消息处理接口*/
    private var mHandler: IMRequestHandler? = null

    /**已连接客户端 Channel 集合*/
    private val mChannelGroup = ConcurrentHashMap<String, Channel>()

    private var mParentGroup: NioEventLoopGroup? = null
    private var mChildGroup: NioEventLoopGroup? = null

    fun build(builder: Builder): IMServerInboundHandler {
        this.mPort = builder.mPort
        this.mReadIdleTime = builder.mReadIdleTime
        this.mWriteIdleTime = builder.mWriteIdleTime
        this.mHeartbeatPingTime = builder.mHeartbeatPingTime
        this.mHandler = builder.mHandler
        return this
    }

    /**启动IM消息服务*/
    fun start() {
        val bootstrap = ServerBootstrap()
        mParentGroup = NioEventLoopGroup()
        mChildGroup = NioEventLoopGroup()
        bootstrap.group(mParentGroup, mChildGroup)
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
        bootstrap.channel(NioServerSocketChannel::class.java)
        bootstrap.childHandler(object : ChannelInitializer<SocketChannel>() {
            @Throws(IMException::class)
            override fun initChannel(ch: SocketChannel) {
                ch.pipeline().addLast(LoggingHandler(LogLevel.TRACE))
                ch.pipeline().addLast(IdleStateHandler(mReadIdleTime, mWriteIdleTime, 0))

                /**消息处理切面-接收消息解码器*/
                ch.pipeline().addLast("SendBodyDecoder", SendBodyDecoder())
                /**消息处理切面-发送消息编码器*/
                ch.pipeline().addLast("SendBodyEncoder", SendBodyEncoder())
                /**消息处理切面-业务处理器，接收到的消息都将被解码为 SendBody */
                ch.pipeline().addLast(this@IMServerInboundHandler)
            }
        })
        /**服务端口绑定与销毁监听*/
        bootstrap.bind(mPort).syncUninterruptibly().channel().closeFuture().addListener {
            destroy()
        }
    }

    /**销毁服务*/
    fun destroy() {
        if (mParentGroup != null && !mParentGroup!!.isShuttingDown && !mParentGroup!!.isShutdown) {
            try {
                mParentGroup!!.shutdownGracefully()
            } catch (ignore: Exception) {
            }
        }
        if (mChildGroup != null && !mChildGroup!!.isShuttingDown && !mChildGroup!!.isShutdown) {
            try {
                mChildGroup!!.shutdownGracefully()
            } catch (ignore: Exception) {
            }
        }
    }

    override fun channelRead0(ctx: ChannelHandlerContext, body: IMSendBody) {
        this.mHandler?.process(IMSession().create(ctx.channel()), body)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        this.mChannelGroup[ctx.channel().id().asShortText()] = ctx.channel()
        this.mHandler?.process(IMSession().create(ctx.channel()), IMSendBody(IMConstant.CLIENT_ACTIVE))
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        this.mChannelGroup.remove(ctx.channel().id().asShortText())
        this.mHandler?.process(IMSession().create(ctx.channel()), IMSendBody(IMConstant.CLIENT_CLOSED))
    }

    @Throws(IMException::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        when {
            evt is IdleStateEvent && evt.state() == IdleState.WRITER_IDLE -> {
                /**每次写入消息，重置上一次通信时间*/
                ctx.channel().attr(AttributeKey.valueOf<Long>(IMConstant.KEY_LAST_HEARTBEAT_TIME))
                        .set(System.currentTimeMillis())
                ctx.channel().writeAndFlush(HeartbeatRequest.instance)
            }
            evt is IdleStateEvent && evt.state() == IdleState.READER_IDLE -> {
                /**每次读取消息，判断上一次通信时间，如果超过预定的时长，则断开连接*/
                val lastTime = ctx.channel()
                        .attr(AttributeKey.valueOf<Long>(IMConstant.KEY_LAST_HEARTBEAT_TIME)).get()
                if (lastTime != null
                        && System.currentTimeMillis() - lastTime > mHeartbeatPingTime) {
                    ctx.channel().close()
                }
                ctx.channel().attr(AttributeKey.valueOf<Long>(IMConstant.KEY_LAST_HEARTBEAT_TIME))
                        .set(null)
            }
        }
    }

    fun getChannelByChannelId(channelId: String?): Channel? {
        return channelId?.let { mChannelGroup[it] }
    }

    /**消息服务参数配置*/
    data class Builder(
            /**消息处理器*/
            var mHandler: IMRequestHandler,
            /**消息服务端口号*/
            var mPort: Int = 0,
            /**心跳响应超时时间，秒*/
            var mHeartbeatPingTime: Int = 30,
            /**连接空闲时间，秒*/
            var mReadIdleTime: Int = 150,
            /**连接空闲时间，秒*/
            var mWriteIdleTime: Int = 120
    )
}