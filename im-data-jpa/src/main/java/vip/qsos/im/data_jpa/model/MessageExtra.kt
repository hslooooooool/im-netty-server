package vip.qsos.im.data_jpa.model

import com.google.gson.Gson
import vip.qsos.im.model.type.EnumSessionType
import java.io.Serializable

/**消息附加信息实体
 * @author : 华清松
 * @param sessionType 聊天类型
 * @param sessionId 聊天会话ID
 * @param timeline 消息时序
 * 当前版本 1
 */
data class MessageExtra constructor(
        var sessionType: EnumSessionType,
        var sessionId: Long,
        var timeline: Long = -1L
) : HashMap<String, Any?>(), Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

    init {
        add("sessionType", sessionType)
        add("sessionId", sessionId)
        add("timeline", timeline)
    }

    fun add(key: String, value: Any?): MessageExtra {
        put(key, value)
        return this
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}