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
 * 发送的公告消息实体
 */
@ApiModel(description = "发送的公告消息实体")
data class SendNoticeForm constructor(
        @ApiModelProperty(value = "消息内容", required = true)
        @NotNull(message = "消息内容不能为空")
        var content: String,
        @ApiModelProperty(value = "消息发送者账号", required = true)
        @NotNull(message = "发送者账号不能为空")
        var sender: String,
        @ApiModelProperty(value = "消息接收者集合账号", required = true)
        @NotNull(message = "接收者账号不能为空")
        var receiver: List<String>,
        @ApiModelProperty(value = "消息标题", required = true)
        var title: String,
        @ApiModelProperty(value = "聊天类型")
        override var chatType: ChatType = ChatType.NOTICE
) : ISendForm, Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    @JsonIgnore
    fun getMessageList(): List<Message> {
        return receiver.map {
            Message(
                    action = "0",
                    title = this.title,
                    content = this.content,
                    sender = this.sender,
                    receiver = it,
                    extra = this.chatType.name,
                    format = Message.Format.JSON.value
            )
        }

    }

}