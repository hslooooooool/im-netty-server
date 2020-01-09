package vip.qsos.im.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.model.db.TableChatGroup
import vip.qsos.im.model.db.TableChatGroupInfo
import vip.qsos.im.model.db.TableChatMessageOfGroup
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.EnumSessionType

@ApiModel(value = "会话业务实体")
data class ChatSessionBo constructor(
        @ApiModelProperty(value = "会话ID")
        var id: Long,
        @ApiModelProperty(value = "会话类型")
        var type: EnumSessionType,
        @ApiModelProperty(value = "标题")
        var title: String,
        @ApiModelProperty(value = "封面")
        var avatar: String? = null,
        @ApiModelProperty(value = "消息内容")
        var content: String? = null
) {

    companion object {
        fun group(
                session: TableChatSession,
                group: TableChatGroup,
                info: TableChatGroupInfo,
                message: TableChatMessageOfGroup?
        ): ChatSessionBo {
            return ChatSessionBo(
                    id = session.sessionId,
                    type = session.sessionType,
                    title = group.name,
                    avatar = info.avatar,
                    content = message?.content
            )
        }
    }
}