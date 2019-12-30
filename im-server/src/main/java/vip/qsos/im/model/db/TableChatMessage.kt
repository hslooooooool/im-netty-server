package vip.qsos.im.model.db

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.Message
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.*

@Entity
@Table
@ApiModel(value = "消息表")
data class TableChatMessage(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "消息ID")
        var messageId: Long = 0,
        @Column(name = "action", nullable = false, length = 16)
        @ApiModelProperty(value = "消息类型")
        var action: String = "0",
        @ApiModelProperty(value = "消息标题")
        @Column(name = "title", length = 16)
        var title: String? = null,
        @ApiModelProperty(value = "消息内容", required = true)
        @Column(name = "content", nullable = false, length = 255)
        var content: String,
        @Column(name = "sender", nullable = false, length = 16)
        @ApiModelProperty(value = "消息发送者账号", required = true)
        var sender: String,
        @Column(name = "receiver", nullable = false, length = 16)
        @ApiModelProperty(value = "消息接收者账号", required = true)
        var receiver: String,
        @Column(name = "format", nullable = false, length = 8)
        @ApiModelProperty(value = "消息数据格式")
        var format: String = Message.Format.PROTOBUF.value,
        @Column(name = "extra", length = 64)
        @ApiModelProperty(value = "附加内容")
        var extra: String? = null,
        @Column(name = "timestamp")
        @ApiModelProperty(value = "消息发送时间")
        var timestamp: LocalDateTime = LocalDateTime.now()
) : AbsTable() {

    @JsonIgnore
    fun getMessage(): Message {
        return Message(
                id = messageId,
                action = action,
                title = title,
                content = content,
                sender = sender,
                receiver = receiver,
                format = format,
                extra = extra,
                timestamp = Date.from(timestamp.atZone(ZoneId.systemDefault())
                        .toInstant())
                        .time
        )
    }

    companion object {
        fun create(message: Message): TableChatMessage {
            return TableChatMessage(
                    messageId = message.id,
                    action = message.action,
                    title = message.title,
                    content = message.content,
                    sender = message.sender,
                    receiver = message.receiver,
                    format = message.format,
                    extra = message.extra,
                    timestamp = Date(message.timestamp).toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
            )
        }
    }
}