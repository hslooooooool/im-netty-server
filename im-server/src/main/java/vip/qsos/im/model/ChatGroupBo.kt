package vip.qsos.im.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.model.db.TableChatSessionOfGroup
import vip.qsos.im.model.type.ChatType

@ApiModel(value = "群组实体")
data class ChatGroupBo(
        @ApiModelProperty(value = "群号")
        var id: Int,
        @ApiModelProperty(value = "群名称")
        var name: String,
        @ApiModelProperty(value = "创建者账号")
        var creator: String,
        @ApiModelProperty(value = "群成员账号集合")
        var member: List<String>,
        @ApiModelProperty(value = "群类型")
        var chatType: ChatType
) {
    companion object {
        fun getBo(table: TableChatSessionOfGroup): ChatGroupBo {
            return ChatGroupBo(
                    id = table.groupId!!,
                    name = table.name,
                    creator = table.creator,
                    chatType = table.chatType,
                    member = table.getAccountList().map { it.account }
            )
        }
    }
}