package vip.qsos.im.model.form

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.gson.Gson
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.AppUserBo
import vip.qsos.im.model.MessageExtra
import vip.qsos.im.model.db.TableUser
import vip.qsos.im.model.type.EnumSessionType
import java.io.Serializable
import javax.validation.constraints.NotNull

/**
 * @author : 华清松
 * 发送基本消息实体
 */
@ApiModel(description = "发送单聊消息")
data class SendMessageInSingleForm constructor(
        @ApiModelProperty(value = "消息会话ID", required = true)
        @NotNull(message = "消息会话ID不能为空")
        var sessionId: Long,
        @ApiModelProperty(value = "消息类型", required = true)
        @NotNull(message = "消息类型不能为空")
        var type: Int = 0,
        @ApiModelProperty(value = "消息内容", required = true)
        @NotNull(message = "消息内容不能为空")
        var data: String,
        @ApiModelProperty(value = "消息发送者账号", required = true)
        @NotNull(message = "发送账号不能为空")
        var sender: String
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
        sChatContent.fields["type"] = type
        // TODO 根据Type识别Content以及ContentDesc
        sChatContent.fields["data"] = data
        sChatContent.fields["desc"] = if (data.length < 20) data else {
            data.substring(0, 20) + "..."
        }
        return sChatContent
    }

    @JsonIgnore
    override var sessionType: EnumSessionType = EnumSessionType.SINGLE

    @JsonIgnore
    fun getMessage(receiver: String, senderUser: TableUser): Message {
        return Message(
                action = "0",
                content = getChatContent().toString(),
                sender = this.sender,
                receiver = receiver,
                extra = MessageExtra(this.sessionType, this.sessionId)
                        .add("sender", AppUserBo.getBo(senderUser))
                        .toString(),
                format = Message.Format.JSON.name
        )
    }

}