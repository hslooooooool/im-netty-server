package vip.qsos.im.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.model.db.TableChatGroup
import vip.qsos.im.model.db.TableChatGroupOfLastRecord
import vip.qsos.im.model.db.TableChatMessage
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.ChatType

@ApiModel(value = "群聊实体")
data class ChatGroupBo(
        @ApiModelProperty(value = "群号")
        var id: Long,
        @ApiModelProperty(value = "群名称")
        var name: String,
        @ApiModelProperty(value = "创建者账号")
        var creator: String,
        @ApiModelProperty(value = "群成员账号集合")
        var member: List<String>,
        @ApiModelProperty(value = "群类型")
        var chatType: ChatType,
        @ApiModelProperty(value = "最后一条消息ID")
        var lastMessageId: Long? = null,
        @ApiModelProperty(value = "最后一条消息时序")
        var lastMessageTimeline: Long? = null,
        @ApiModelProperty(value = "最后一条消息")
        var lastMessage: TableChatMessage? = null
) {
    companion object {
        fun getBo(session: TableChatSession, group: TableChatGroup): ChatGroupBo {
            return ChatGroupBo(
                    id = group.groupId,
                    name = group.name,
                    creator = session.creator,
                    chatType = session.chatType,
                    member = session.getAccountList().map { it.account }
            )
        }
    }

    fun addLastRecord(record: TableChatGroupOfLastRecord): ChatGroupBo {
        this.lastMessageId = record.lastMessageId
        this.lastMessageTimeline = record.lastMessageTimeline
        return this
    }
}