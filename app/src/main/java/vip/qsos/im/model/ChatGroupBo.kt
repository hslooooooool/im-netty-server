package vip.qsos.im.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.model.db.TableChatGroup
import vip.qsos.im.model.db.TableChatGroupInfo
import vip.qsos.im.model.db.TableChatMessageOfGroup
import vip.qsos.im.model.db.TableChatSession
import java.time.LocalDateTime

@ApiModel(value = "群聊业务实体")
data class ChatGroupBo constructor(
        @ApiModelProperty(value = "会话ID")
        var sessionId: Long,
        @ApiModelProperty(value = "群聊ID")
        var groupId: Long,
        @ApiModelProperty(value = "群名称")
        var name: String,
        @ApiModelProperty(value = "群封面")
        var avatar: String? = null,
        @ApiModelProperty(value = "最后一条消息")
        var lastMessage: LastMessage? = null
) {

    companion object {
        fun create(
                session: TableChatSession,
                group: TableChatGroup,
                info: TableChatGroupInfo,
                message: TableChatMessageOfGroup?,
                sender: AppUser?
        ): ChatGroupBo {
            return ChatGroupBo(
                    sessionId = session.sessionId,
                    groupId = group.groupId,
                    name = group.name,
                    avatar = info.avatar,
                    lastMessage = message?.let {
                        /**最后一条消息不为空,则发送人肯定不为空*/
                        sender!!
                        LastMessage(
                                sender.name,
                                it.messageId,
                                it.timeline,
                                it.timestamp,
                                it.content
                        )
                    }
            )
        }
    }

    data class LastMessage(
            @ApiModelProperty(value = "发送人名称")
            var senderName: String? = null,
            @ApiModelProperty(value = "消息ID")
            var messageId: Long? = null,
            @ApiModelProperty(value = "消息时序")
            var timeline: Long? = null,
            @ApiModelProperty(value = "发送时间")
            var timestamp: LocalDateTime,
            @ApiModelProperty(value = "消息内容")
            var content: String
    )
}