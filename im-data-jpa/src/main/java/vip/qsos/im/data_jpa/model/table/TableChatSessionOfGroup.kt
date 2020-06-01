package vip.qsos.im.data_jpa.model.table

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.model.db.AbsTable
import javax.persistence.*

@Entity
@Table(name = "table_chat_group")
@ApiModel(value = "群聊表")
data class TableChatSessionOfGroup constructor(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "群号")
        var groupId: Long = -1L,
        @Column(name = "sessionId")
        @ApiModelProperty(value = "会话ID")
        var sessionId: Long = -1L,
        @Column(name = "name", nullable = false, length = 16)
        @ApiModelProperty(value = "群名称")
        var name: String = "",
        @Column(name = "creator")
        @ApiModelProperty(value = "群描述")
        var desc: String? = null
) : AbsTable()