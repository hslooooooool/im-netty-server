package vip.qsos.im.model

import cn.teaey.apns4j.protocol.ApnsPayload

/**
 * @author : 华清松
 * 苹果推送实体
 */
class ApnsPayloadCompat : ApnsPayload() {
    var action: String? = null
    var content: String? = null
    var sender: String? = null
    var receiver: String? = null
    var format: String? = null

    override fun toJsonString(): String {
        return "{\"aps\": " +
                "{\"message\": " +
                "{\"action\":\"$action\"," +
                "\"content\":\"$content\"," +
                "\"sender\":\"$sender\"," +
                "\"receiver\":\"$receiver\"," +
                "\"format\":\"$format\"" +
                "}," +
                "\"content-available\": 1" +
                "}" +
                "}"
    }
}