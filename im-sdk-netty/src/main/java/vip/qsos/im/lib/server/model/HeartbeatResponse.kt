package vip.qsos.im.lib.server.model

import java.io.Serializable

/**
 * @author : 华清松
 * 客户端心跳响应实体
 */
class HeartbeatResponse private constructor() : Serializable {

    companion object {
        private const val serialVersionUID = 1L
        const val TAG = "CLIENT_HEARTBEAT_RESPONSE"
        const val CLIENT_HEARTBEAT_RESPONSE = "CR"
        val instance = HeartbeatResponse()
    }

    override fun toString(): String {
        return TAG
    }

}