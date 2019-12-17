package com.farsunset.cim.util

import cn.teaey.apns4j.protocol.ApnsPayload

class ApnsPayloadCompat : ApnsPayload() {
    var action: String? = null
    var content: String? = null
    var sender: String? = null
    var format: String? = null
    var receiver: String? = null

    override fun toJsonString(): String {
        return String.format(DATA_FORMAT, action, content, sender, receiver, format)
    }

    companion object {
        private const val DATA_FORMAT = "{\"aps\": " +
                "{\"message\": " +
                "{\"action\":\"%s\",\"content\":\"%s\",\"sender\":\"%s\",\"receiver\":\"%s\",\"format\":\"%s\"" +
                "},\"content-available\": 1" +
                "}" +
                "}"
    }
}