package vip.qsos.im.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.model.db.TableChatSessionOfGroup
import vip.qsos.im.model.db.TableChatSessionOfGroupInfo
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.EnumSessionType

@ApiModel(value = "群聊实体")
data class ChatGroupBo constructor(
        @ApiModelProperty(value = "群号")
        var id: Long,
        @ApiModelProperty(value = "群名称")
        var name: String,
        @ApiModelProperty(value = "创建者账号")
        var creator: String,
        @ApiModelProperty(value = "群成员账号集合")
        var member: List<String>,
        @ApiModelProperty(value = "群类型")
        var sessionType: EnumSessionType,
        @ApiModelProperty(value = "最后一条消息ID")
        var lastMessageId: Long? = null
) {
    companion object {
        fun getBo(session: TableChatSession, group: TableChatSessionOfGroup): ChatGroupBo {
            return ChatGroupBo(
                    id = group.groupId,
                    name = group.name,
                    creator = session.creator,
                    member = session.getAccountList().map { it.account },
                    sessionType = session.sessionType
            )
        }
    }

    fun setInfo(info: TableChatSessionOfGroupInfo): ChatGroupBo {
        this.lastMessageId = info.lastMessageId
        return this
    }

}