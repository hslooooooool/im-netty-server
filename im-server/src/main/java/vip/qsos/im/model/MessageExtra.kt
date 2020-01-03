package vip.qsos.im.model

import com.google.gson.Gson
import vip.qsos.im.model.type.ChatType
import java.io.Serializable

/**
 * @author : 华清松
 * 消息附加信息实体
 * @param chatType 聊天类型
 * @param belongId 所属ID，根据聊天类型区分
 * 当前版本 1
 */
data class MessageExtra constructor(
        var chatType: ChatType,
        var belongId: String
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}