package vip.qsos.im.lib.server.model

import com.google.protobuf.InvalidProtocolBufferException
import io.netty.channel.Channel
import io.netty.handler.codec.EncoderException
import io.netty.util.AttributeKey
import vip.qsos.im.lib.model.proto.SessionProto
import vip.qsos.im.lib.server.constant.IMConstant
import java.net.SocketAddress

/**
 * @author : 华清松
 * 消息会话包装类。集群时，将此对象存入表中
 */
class Session : IProtobufAble {

    companion object {
        private const val serialVersionUID = 1L
        const val PROTOCOL = "protocol"
        /**消息客户端类型 */
        const val WEBSOCKET = "websocket"
        const val NATIVE_APP = "nativeapp"
        const val HOST = "HOST"

        /**客户端连接状态 */
        const val STATE_ENABLED = 0
        const val STATE_DISABLED = 1

        /**苹果推送服务开关 */
        const val APNS_ON = 1
        const val APNS_OFF = 0

        /**连接客户端类型 */
        const val CHANNEL_IOS = "ios"
        const val CHANNEL_ANDROID = "android"
        const val CHANNEL_WINDOWS = "windows"
        const val CHANNEL_WP = "wp"
        const val CHANNEL_BROWSER = "browser"

        @Throws(InvalidProtocolBufferException::class)
        fun decode(protobufBody: ByteArray?): Session? {
            if (protobufBody == null) {
                return null
            }
            val proto = SessionProto.Model.parseFrom(protobufBody)
            val session = Session()
            session.id = proto.id
            session.apns = proto.apns
            session.bindTime = proto.bindTime
            session.channel = proto.channel
            session.clientVersion = proto.clientVersion
            session.deviceId = proto.deviceId
            session.deviceModel = proto.deviceModel
            session.host = proto.host
            session.latitude = proto.latitude
            session.longitude = proto.longitude
            session.location = proto.location
            session.nid = proto.nid
            session.systemVersion = proto.systemVersion
            session.state = proto.state
            session.setAccount(proto.account)
            return session
        }
    }

    /**当前连接通道*/
    var session: Channel? = null
    /**数据库主键ID*/
    var id: Long? = null
    /**session绑定的用户账号*/
    private var account: String? = null
    /**session在本台服务器上的ID*/
    var nid: String? = null
    /**客户端 ID (设备号码+应用包名),ios为 device token */
    var deviceId: String? = null
    /**session绑定的服务器IP*/
    var host: String? = null
    /**客户端设备类型*/
    var channel: String? = null
    /**客户端设备型号*/
    var deviceModel: String? = null
    /**客户端应用版本*/
    var clientVersion: String? = null
    /**客户端系统版本*/
    var systemVersion: String? = null
    /**客户端登录时间*/
    var bindTime: Long? = null
    /**客户端经度*/
    var longitude: Double? = null
    /**客户端维度*/
    var latitude: Double? = null
    /**客户端位置*/
    var location: String? = null
    /**apns推送状态*/
    var apns = 0
    /**客户端在线状态*/
    var state = 0

    constructor()

    constructor(session: Channel) {
        this.session = session
        this.nid = session.id().asShortText()
    }

    fun getAccount(): String? {
        return account
    }

    fun setAccount(account: String?) {
        this.account = account
        setAttribute(IMConstant.KEY_ACCOUNT, account)
    }

    fun setAttribute(key: String?, value: Any?) {
        session?.attr(AttributeKey.valueOf<Any>(key))?.set(value)
    }

    fun containsAttribute(key: String?): Boolean {
        return session?.hasAttr(AttributeKey.valueOf<Any>(key)) ?: false
    }

    fun getAttribute(key: String?): Any? {
        return session?.attr(AttributeKey.valueOf<Any>(key))?.get()
    }

    fun removeAttribute(key: String?) {
        session?.attr(AttributeKey.valueOf<Any>(key))?.set(null)
    }

    val remoteAddress: SocketAddress?
        get() = session?.remoteAddress()

    @Throws(EncoderException::class)
    fun write(msg: Any?): Boolean {
        return if (session != null && session!!.isActive) {
            session!!.writeAndFlush(msg).awaitUninterruptibly(5000)
        } else false
    }

    val isConnected: Boolean
        get() = session != null && session!!.isActive || state == STATE_ENABLED

    fun closeNow() {
        session?.close()
    }

    fun closeOnFlush() {
        session?.close()
    }

    val isIOSChannel: Boolean
        get() = channel == CHANNEL_IOS

    val isAndroidChannel: Boolean
        get() = channel == CHANNEL_ANDROID

    val isWindowsChannel: Boolean
        get() = channel == CHANNEL_WINDOWS

    val isApnsOpen: Boolean
        get() = apns == APNS_ON

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Session) {
            (other.deviceId == deviceId && other.nid == nid && other.host == host)
        } else false
    }

    override val type: Byte = IMConstant.ProtobufType.SESSION

    override val byteArray: ByteArray
        get() {
            val builder = SessionProto.Model.newBuilder()
            id?.let { builder.id = it }
            account?.let { builder.account = it }
            builder.nid = nid
            builder.deviceId = deviceId
            builder.host = host
            builder.channel = channel
            builder.deviceModel = deviceModel
            builder.clientVersion = clientVersion
            builder.systemVersion = systemVersion
            bindTime?.let { builder.bindTime = it }
            longitude?.let { builder.longitude = it }
            latitude?.let { builder.latitude = it }
            location?.let { builder.location = it }
            builder.state = state
            builder.apns = apns
            return builder.build().toByteArray()
        }
}