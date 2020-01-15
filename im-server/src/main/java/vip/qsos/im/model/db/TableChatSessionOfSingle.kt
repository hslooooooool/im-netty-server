package vip.qsos.im.model.db

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

@Entity
@Table(name = "table_chat_single")
@ApiModel(value = "单聊表")
data class TableChatSessionOfSingle constructor(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "群号")
        var singleId: Long = -1L,
        @Column(name = "sessionId")
        @ApiModelProperty(value = "会话ID")
        var sessionId: Long = -1L
) : AbsTable()