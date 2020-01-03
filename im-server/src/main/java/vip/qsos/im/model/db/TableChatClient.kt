package vip.qsos.im.model.db

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.Session
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "table_chat_client")
@ApiModel(value = "客户端连接表")
data class TableChatClient constructor(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "会话ID")
        var sessionId: Long? = null,

        @Column(name = "nid", unique = true, nullable = false, length = 16)
        @ApiModelProperty(value = "channel ID", required = false)
        var nid: String? = null,
        @Column(name = "host", nullable = false, length = 16)
        @ApiModelProperty(value = "channel 绑定的服务器IP，用于分布式区分", required = false)
        var host: String? = null,
        @Column(name = "account", unique = true, nullable = false, length = 16)
        @ApiModelProperty(value = "账号", required = true)
        var account: String = "",

        @Column(name = "deviceId", nullable = false, length = 32)
        @ApiModelProperty(value = "客户端 ID (设备号码+应用包名),ios为 device token", required = false)
        var deviceId: String? = null,
        @Column(name = "device_type", length = 16)
        @ApiModelProperty(value = "客户端设备类型", required = false)
        var deviceType: String? = null,
        @Column(name = "device_model")
        @ApiModelProperty(value = "客户端设备型号", required = false)
        var deviceModel: String? = null,

        @Column(name = "client_version", nullable = false, length = 16)
        @ApiModelProperty(value = "客户端应用版本", required = false)
        var clientVersion: String? = null,
        @Column(name = "system_version", nullable = false, length = 16)
        @ApiModelProperty(value = "客户端系统版本", required = false)
        var systemVersion: String? = null,

        @Column(name = "bind_ime")
        @ApiModelProperty(value = "客户端登录时间", required = false)
        var bindTime: LocalDateTime? = null,
        @Column(name = "apns", nullable = false, length = 8)
        @ApiModelProperty(value = "apns推送状态", required = false)
        var apns: Int = 1,
        @Column(name = "state", nullable = false, length = 8)
        @ApiModelProperty(value = "客户端在线状态", required = false)
        var state: Int = 0,

        @Column(name = "longitude")
        @ApiModelProperty(value = "客户端经度", required = false)
        var longitude: Double? = null,
        @Column(name = "latitude")
        @ApiModelProperty(value = "客户端维度", required = false)
        var latitude: Double? = null,
        @Column(name = "location", length = 64)
        @ApiModelProperty(value = "客户端位置", required = false)
        var location: String? = null
) : AbsTable() {

    @JsonIgnore
    fun getSession(): Session {
        return Session(
                id = sessionId, nid = nid, host = host, account = account, deviceId = deviceId, deviceModel = deviceModel,
                deviceType = deviceType, clientVersion = clientVersion,
                systemVersion = systemVersion, bindTime = bindTime, apns = apns, state = state,
                longitude = longitude, latitude = latitude, location = location
        )
    }

    companion object {
        fun create(session: Session): TableChatClient {
            return TableChatClient(
                    sessionId = session.id,
                    nid = session.nid,
                    host = session.host,
                    account = session.getAccount(),
                    deviceId = session.deviceId,
                    deviceModel = session.deviceModel,
                    deviceType = session.deviceType,
                    clientVersion = session.clientVersion,
                    systemVersion = session.systemVersion,
                    bindTime = session.bindTime,
                    apns = session.apns,
                    state = session.state,
                    longitude = session.longitude,
                    latitude = session.latitude,
                    location = session.location
            )
        }
    }
}