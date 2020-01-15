package vip.qsos.im.lib.server.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.protobuf.InvalidProtocolBufferException
import io.netty.channel.Channel
import io.netty.handler.codec.EncoderException
import io.netty.util.AttributeKey
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.model.proto.SessionProto
import vip.qsos.im.lib.server.config.IMConstant
import java.net.SocketAddress
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/**
 * @author : 华清松
 * 消息会话实体
 */
@ApiModel(description = "建立会话的终端信息实体")
data class SessionClientBo(
        @ApiModelProperty(value = "数据库主键ID")
        var id: Long = -1L,
        @ApiModelProperty(value = "channel ID")
        var nid: String? = null,
        @ApiModelProperty(value = "客户端 ID (设备号码+应用包名),ios为 device token")
        var deviceId: String? = null,
        @ApiModelProperty(value = "channel 绑定的服务器IP，用于分布式区分")
        var host: String? = null,
        @ApiModelProperty(value = "客户端设备类型")
        var deviceType: String? = null,
        @ApiModelProperty(value = "客户端设备型号")
        var deviceModel: String? = null,
        @ApiModelProperty(value = "客户端应用版本")
        var clientVersion: String? = null,
        @ApiModelProperty(value = "客户端系统版本")
        var systemVersion: String? = null,
        @ApiModelProperty(value = "客户端登录时间")
        var bindTime: LocalDateTime? = null,
        @ApiModelProperty(value = "客户端经度")
        var longitude: Double? = null,
        @ApiModelProperty(value = "客户端维度")
        var latitude: Double? = null,
        @ApiModelProperty(value = "客户端位置")
        var location: String? = null,
        @ApiModelProperty(value = "apns推送状态")
        var apns: Int = 1,
        @ApiModelProperty(value = "客户端在线状态")
        var state: Int = 0,
        @ApiModelProperty(value = "channel 绑定的用户账号")
        private var account: String = ""
) : IProtobufAble {

    companion object {
        private const val serialVersionUID = 1L

        /**消息客户端类型*/
        const val CHANNEL_TYPE = "channel_type"
        const val WEBSOCKET = "websocket"
        const val NATIVE_APP = "native_app"

        /**客户端连接状态*/
        const val STATE_ENABLED = 0
        const val STATE_DISABLED = 1

        /**苹果推送服务开关*/
        const val APNS_ON = 0
        const val APNS_OFF = 1

        /**连接客户端类型*/
        const val CHANNEL_WP = "wp"
        const val CHANNEL_IOS = "ios"
        const val CHANNEL_ANDROID = "android"
        const val CHANNEL_WINDOWS = "windows"
        const val CHANNEL_BROWSER = "browser"
    }

    /**当前连接通道*/
    @JsonIgnore
    var channel: Channel? = null

    fun create(channel: Channel): SessionClientBo {
        this.channel = channel
        this.nid = channel.id().asShortText()
        return this
    }

    fun getAccount(): String {
        return account
    }

    fun setAccount(account: String) {
        this.account = account
        setAttribute(IMConstant.KEY_ACCOUNT, account)
    }

    fun setAttribute(key: String, value: Any?) {
        channel?.attr(AttributeKey.valueOf<Any>(key))?.set(value)
    }

    fun containsAttribute(key: String): Boolean {
        return channel?.hasAttr(AttributeKey.valueOf<Any>(key)) ?: false
    }

    fun <T> getAttribute(key: String?): T? {
        return channel?.attr(AttributeKey.valueOf<T>(key))?.get()
    }

    fun removeAttribute(key: String) {
        channel?.attr(AttributeKey.valueOf<Any>(key))?.set(null)
    }

    val remoteAddress: SocketAddress?
        get() = channel?.remoteAddress()

    @Throws(EncoderException::class)
    fun write(msg: Any?): Boolean {
        return if (channel != null && channel!!.isActive) {
            channel!!.writeAndFlush(msg).awaitUninterruptibly(5000)
        } else false
    }

    val isConnected: Boolean
        get() = channel != null && channel!!.isActive || state == STATE_ENABLED

    fun closeNow() {
        channel?.close()
    }

    fun closeOnFlush() {
        channel?.close()
    }

    val isIOSChannel: Boolean
        get() = deviceType == CHANNEL_IOS

    val isAndroidChannel: Boolean
        get() = deviceType == CHANNEL_ANDROID

    val isWindowsChannel: Boolean
        get() = deviceType == CHANNEL_WINDOWS

    val isApnsOpen: Boolean
        get() = apns == APNS_ON

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is SessionClientBo) {
            (other.deviceId == deviceId && other.nid == nid && other.host == host)
        } else false
    }

    override val type: Byte = IMConstant.ProtobufType.SESSION

    override val byteArray: ByteArray
        @Throws(InvalidProtocolBufferException::class)
        get() {
            val builder = SessionProto.Model.newBuilder()
            id?.let { builder.id = it }
            builder.account = account
            builder.nid = nid
            builder.deviceId = deviceId
            builder.host = host
            builder.channel = deviceType
            builder.deviceModel = deviceModel
            builder.clientVersion = clientVersion
            builder.systemVersion = systemVersion
            bindTime?.let {
                builder.bindTime = Date.from(it.atZone(ZoneId.systemDefault())
                        .toInstant()).time
            }
            longitude?.let { builder.longitude = it }
            latitude?.let { builder.latitude = it }
            location?.let { builder.location = it }
            builder.state = state
            builder.apns = apns
            return builder.build().toByteArray()
        }
}