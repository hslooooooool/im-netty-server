package vip.qsos.im.data_jpa.model.table

import io.swagger.annotations.ApiModel
import vip.qsos.im.lib.server.model.IMMessage
import vip.qsos.im.model.MessageExtra
import vip.qsos.im.model.type.EnumSessionType
import java.time.ZoneId
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "table_chat_message_group")
@ApiModel(value = "群聊消息表")
class TableChatMessageOfGroup : AbsTableMessage() {

    override val extra: MessageExtra
        get() = MessageExtra(EnumSessionType.GROUP, sessionId, timeline)

    companion object {
        fun create(sessionId: Long, message: IMMessage, timeline: Long = -1L): TableChatMessageOfGroup {
            val table = TableChatMessageOfGroup()
            table.messageId = message.id
            table.timeline = timeline
            table.title = message.title
            table.content = message.content
            table.sender = message.sender
            table.receiver = message.receiver
            table.sessionId = sessionId
            table.format = IMMessage.Format.JSON
            table.timestamp = Date(message.timestamp).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            return table
        }
    }
}