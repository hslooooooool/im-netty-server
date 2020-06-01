package vip.qsos.im.model.form

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.gson.Gson
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.IMMessage
import vip.qsos.im.model.type.EnumSessionType
import java.io.Serializable
import javax.validation.constraints.NotNull

/**发送指令消息实体
 * @author : 华清松
 */
@ApiModel(description = "发送指令消息")
data class SendMessageOfOrderForm constructor(
        @ApiModelProperty(value = "消息指令", required = true)
        @NotNull(message = "消息指令不能为空")
        var action: String,
        @ApiModelProperty(value = "消息发送者账号", required = true)
        @NotNull(message = "发送账号不能为空")
        var sender: String,
        @ApiModelProperty(value = "消息接收账号", required = true)
        @NotNull(message = "接收账号不能为空")
        var receiver: String,
        @ApiModelProperty(value = "消息内容", required = true)
        @NotNull(message = "消息内容不能为空")
        var content: String,
        @ApiModelProperty(value = "消息类型", required = true)
        @NotNull(message = "消息类型不能为空")
        var contentType: Int = 0,
        @ApiModelProperty(value = "消息附加信息")
        var extra: String? = null
) : ISendForm, Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

    data class ChatContent(
            var fields: HashMap<String, Any?> = HashMap()
    ) {
        override fun toString(): String {
            return Gson().toJson(fields)
        }
    }

    @JsonIgnore
    fun getChatContent(): ChatContent {
        val sChatContent = ChatContent()
        sChatContent.fields["content"] = content
        sChatContent.fields["contentType"] = contentType
        sChatContent.fields["contentDesc"] = if (content.length < 20) content else {
            content.substring(0, 20) + "..."
        }
        return sChatContent
    }

    @JsonIgnore
    override var sessionType: EnumSessionType = EnumSessionType.ORDER

    @JsonIgnore
    fun getMessage(receiver: String? = null): IMMessage {
        return IMMessage(
                action = sessionType.name,
                content = getChatContent().toString(),
                sender = this.sender,
                receiver = receiver ?: this.receiver,
                extra = extra,
                format = IMMessage.Format.JSON.name
        )
    }

}