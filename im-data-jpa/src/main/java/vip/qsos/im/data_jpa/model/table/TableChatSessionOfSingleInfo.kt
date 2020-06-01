package vip.qsos.im.data_jpa.model.table

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.model.db.AbsTable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "table_chat_single_info")
@ApiModel(value = "单聊信息拓展表")
data class TableChatSessionOfSingleInfo constructor(
        @Id
        @Column(name = "id")
        @ApiModelProperty(value = "群号")
        var singleId: Long = -1L,
        @Column(name = "last_message_id")
        @ApiModelProperty(value = "最后一条消息ID")
        var lastMessageId: Long? = null
) : AbsTable()