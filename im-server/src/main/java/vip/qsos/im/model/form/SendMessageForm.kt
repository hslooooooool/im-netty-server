package vip.qsos.im.model.form

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.gson.Gson
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.type.ChatType
import javax.validation.constraints.NotNull

/**
 * @author : 华清松
 * 发送的消息实体
 */
@ApiModel(description = "发送的消息实体")
data class SendMessageForm constructor(
        @ApiModelProperty(value = "消息类型，如0:文本、1:文件等，默认：0")
        @NotNull(message = "消息类型不能为空")
        var action: String = "0",
        @ApiModelProperty(value = "消息内容", required = true)
        @NotNull(message = "消息内容不能为空")
        var content: String,
        @ApiModelProperty(value = "消息发送者账号", required = true)
        @NotNull(message = "发送者账号不能为空")
        var sender: String,
        @ApiModelProperty(value = "消息接收者账号", required = true)
        @NotNull(message = "接收者账号不能为空")
        var receiver: String,
        @ApiModelProperty(value = "消息标题")
        var title: String? = null,
        @ApiModelProperty(value = "附加内容")
        var extra: Extra? = null
) {
    @JsonIgnore
    fun getMessage(): Message {
        return Message(
                action = this.action,
                title = this.title,
                content = this.content,
                sender = this.sender,
                receiver = this.receiver,
                extra = this.extra.toString(),
                format = Message.Format.JSON.value
        )
    }

    data class Extra(var chatType: ChatType) {
        override fun toString(): String {
            return Gson().toJson(this)
        }
    }

}