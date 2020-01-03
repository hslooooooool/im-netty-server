package vip.qsos.im.model.form

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.type.ChatType
import java.io.Serializable
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
        @NotNull(message = "发送账号不能为空")
        var sender: String,
        @ApiModelProperty(value = "消息接收群ID", required = true)
        @NotNull(message = "接收账号不能为空")
        var groupId: String,
        @ApiModelProperty(value = "消息标题")
        var title: String? = null,
        @ApiModelProperty(value = "聊天类型", required = true, example = "SINGLE")
        override var chatType: ChatType = ChatType.SINGLE
) : ISendForm, Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    @JsonIgnore
    fun getMessage(receiver: String? = null): Message {
        return Message(
                action = this.action,
                title = this.title,
                content = this.content,
                sender = this.sender,
                receiver = receiver ?: this.groupId,
                extra = this.chatType.name,
                format = Message.Format.JSON.value
        )
    }

}