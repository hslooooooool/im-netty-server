package vip.qsos.im.model.db

import io.swagger.annotations.ApiModel
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.MessageExtra
import vip.qsos.im.model.type.EnumSessionType
import java.time.ZoneId
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "table_chat_message_single")
@ApiModel(value = "单聊消息表")
class TableChatMessageOfSingle : AbsTableChatMessage() {

    override val extra: MessageExtra = MessageExtra(EnumSessionType.SINGLE, sessionId)

    companion object {
        fun create(sessionId: Long, message: Message, timeline: Long = -1L): TableChatMessageOfSingle {
            val table = TableChatMessageOfSingle()
            table.messageId = message.id
            table.timeline = timeline
            table.title = message.title
            table.content = message.content
            table.sender = message.sender
            table.receiver = message.receiver
            table.sessionId = sessionId
            table.format = Message.Format.JSON
            table.timestamp = Date(message.timestamp).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            return table
        }
    }
}