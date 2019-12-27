package vip.qsos.im.lib.server.model

import vip.qsos.im.lib.server.config.IMConstant

/**
 * @author : 华清松
 * 客户端心跳响应实体
 */
class HeartbeatResponse private constructor() : IProtobufAble {

    companion object {
        private const val serialVersionUID = 1L
        const val TAG = "CLIENT_HEARTBEAT_RESPONSE"
        const val CLIENT_HEARTBEAT_RESPONSE = "CR"
        val instance = HeartbeatResponse()
    }

    override val byteArray: ByteArray = CLIENT_HEARTBEAT_RESPONSE.toByteArray()

    override val type: Byte = IMConstant.ProtobufType.HEART_CR

    override fun toString(): String {
        return TAG
    }

}