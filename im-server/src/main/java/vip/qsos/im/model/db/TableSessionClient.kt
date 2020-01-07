package vip.qsos.im.model.db

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.SessionClient
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "table_session_client")
@ApiModel(value = "客户端会话连接表")
data class TableSessionClient constructor(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "连接ID")
        var id: Long = -1L,

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
    fun getSession(): SessionClient {
        return SessionClient(
                id = id, nid = nid, host = host, account = account, deviceId = deviceId, deviceModel = deviceModel,
                deviceType = deviceType, clientVersion = clientVersion,
                systemVersion = systemVersion, bindTime = bindTime, apns = apns, state = state,
                longitude = longitude, latitude = latitude, location = location
        )
    }

    companion object {
        fun create(sessionClient: SessionClient): TableSessionClient {
            return TableSessionClient(
                    id = sessionClient.id,
                    nid = sessionClient.nid,
                    host = sessionClient.host,
                    account = sessionClient.getAccount(),
                    deviceId = sessionClient.deviceId,
                    deviceModel = sessionClient.deviceModel,
                    deviceType = sessionClient.deviceType,
                    clientVersion = sessionClient.clientVersion,
                    systemVersion = sessionClient.systemVersion,
                    bindTime = sessionClient.bindTime,
                    apns = sessionClient.apns,
                    state = sessionClient.state,
                    longitude = sessionClient.longitude,
                    latitude = sessionClient.latitude,
                    location = sessionClient.location
            )
        }
    }
}