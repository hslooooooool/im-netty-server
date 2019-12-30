package vip.qsos.im.model

import cn.teaey.apns4j.protocol.ApnsPayload

/**
 * @author : 华清松
 * 苹果推送实体
 */
data class ApnsPayloadCompat(
        var action: String,
        var content: String,
        var sender: String,
        var receiver: String,
        var format: String
) : ApnsPayload() {

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