package vip.qsos.im.model.db

import io.swagger.annotations.ApiModelProperty
import jdk.nashorn.internal.ir.annotations.Ignore
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.MessageExtra
import vip.qsos.im.model.type.ChatType
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class AbsTableChatMessage : AbsTable() {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "消息ID")
    var messageId: Long = -1L
    @ApiModelProperty(value = "消息标题")
    @Column(name = "title", length = 16)
    var title: String? = null
    @Column(name = "timeline")
    @ApiModelProperty(value = "消息时序，同一场景下递增，唯一")
    var timeline: Long = -1L
    @ApiModelProperty(value = "消息内容", required = true)
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String = ""
    @Column(name = "sender", nullable = false, length = 16)
    @ApiModelProperty(value = "消息发送者账号", required = true)
    var sender: String = ""
    @Column(name = "receiver", nullable = false, length = 16)
    @ApiModelProperty(value = "消息接收者账号", required = true)
    var receiver: String = ""
    @Column(name = "group_id")
    @ApiModelProperty(value = "群ID")
    var groupId: String = ""
    @Column(name = "format", nullable = false, length = 8)
    @ApiModelProperty(value = "消息数据格式")
    var format: Message.Format = Message.Format.PROTOBUF
    @Column(name = "timestamp")
    @ApiModelProperty(value = "消息发送时间")
    var timestamp: LocalDateTime = LocalDateTime.now()

    @Ignore
    protected abstract val extra: MessageExtra

    @Ignore
    fun getMessage(): Message {
        return Message(
                id = messageId,
                action = ChatType.GROUP.name,
                title = title,
                content = content,
                sender = sender,
                receiver = receiver,
                format = format.name,
                extra = extra.toString(),
                timestamp = Date.from(gmtCreate.atZone(ZoneId.systemDefault())
                        .toInstant())
                        .time
        )
    }

}