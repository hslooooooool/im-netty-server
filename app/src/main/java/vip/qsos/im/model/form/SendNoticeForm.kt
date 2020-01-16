package vip.qsos.im.model.form

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.type.EnumSessionType
import java.io.Serializable
import javax.validation.constraints.NotNull

/**
 * @author : 华清松
 * 发送的公告消息实体
 */
@ApiModel(description = "公告消息发送表单")
data class SendNoticeForm constructor(
        @ApiModelProperty(value = "消息发送者账号", required = true)
        @NotNull(message = "发送者账号不能为空")
        var sender: String,
        @ApiModelProperty(value = "消息接收者集合账号", required = true)
        @NotNull(message = "接收者账号不能为空")
        var receiver: List<String>,
        @ApiModelProperty(value = "消息内容", required = true)
        @NotNull(message = "消息内容不能为空")
        var content: String,
        @ApiModelProperty(value = "消息类型", required = true)
        @NotNull(message = "消息类型不能为空")
        var contentType: Int = 0
) : ISendForm, Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

    @JsonIgnore
    override var sessionType: EnumSessionType = EnumSessionType.NOTICE

    @JsonIgnore
    fun getMessageList(): List<Message> {
        return receiver.map {
            Message(
                    action = EnumSessionType.NOTICE.name,
                    title = "公告",
                    content = this.content,
                    sender = this.sender,
                    receiver = it,
                    extra = this.sessionType.name,
                    format = Message.Format.JSON.name
            )
        }

    }

}