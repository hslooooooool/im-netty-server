package vip.qsos.im.model.db

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "table_chat_group_last_record")
@ApiModel(value = "群组最后的消息记录表")
data class TableChatGroupOfLastRecord constructor(
        @Id
        @Column(name = "id")
        @ApiModelProperty(value = "群号")
        var groupId: Long = -1L,
        @Column(name = "last_message_id")
        @ApiModelProperty(value = "最后一条消息ID")
        var lastMessageId: Long? = null,
        @Column(name = "last_message_timeline")
        @ApiModelProperty(value = "最后一条消息时序")
        var lastMessageTimeline: Long? = null
) : AbsTable()