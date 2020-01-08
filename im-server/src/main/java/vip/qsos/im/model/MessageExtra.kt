package vip.qsos.im.model

import com.google.gson.Gson
import vip.qsos.im.model.type.EnumSessionType
import java.io.Serializable

/**
 * @author : 华清松
 * 消息附加信息实体
 * @param sessionType 聊天类型
 * @param sessionId 聊天会话ID
 * 当前版本 1
 */
data class MessageExtra constructor(
        var sessionType: EnumSessionType,
        var sessionId: Long
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}