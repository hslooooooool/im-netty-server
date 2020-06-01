package vip.qsos.im.data_jpa.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.data_jpa.model.table.TableChatSession
import vip.qsos.im.data_jpa.model.table.TableChatSessionOfSingle
import vip.qsos.im.data_jpa.model.table.TableChatSessionOfSingleInfo
import vip.qsos.im.model.type.EnumSessionType

@ApiModel(value = "单聊实体")
data class ChatSingleBo constructor(
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

        fun getBo(session: TableChatSession, group: TableChatSessionOfSingle): ChatSingleBo {
            return ChatSingleBo(
                    id = group.singleId,
                    name = "单聊${group.singleId}",
                    creator = session.creator,
                    member = session.getAccountList().map { it.account },
                    sessionType = session.sessionType
            )
        }
    }

    fun setInfo(info: TableChatSessionOfSingleInfo): ChatSingleBo {
        this.lastMessageId = info.lastMessageId
        return this
    }

}