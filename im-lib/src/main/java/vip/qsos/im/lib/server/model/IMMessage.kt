package vip.qsos.im.lib.server.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.protobuf.InvalidProtocolBufferException
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.model.proto.MessageProto
import vip.qsos.im.lib.server.config.IMConstant

/**自定义消息对象
 * @author : 华清松
 */
@ApiModel(description = "消息实体")
data class IMMessage constructor(
        @ApiModelProperty(value = "消息ID")
        var id: Long = -1L,
        @ApiModelProperty(value = "指令码")
        var action: String,
        @ApiModelProperty(value = "消息标题")
        var title: String? = null,
        @ApiModelProperty(value = "消息内容，content 根据 format 数据格式进行解析", required = true)
        var content: String,
        @ApiModelProperty(value = "消息发送者账号", required = true)
        var sender: String,
        @ApiModelProperty(value = "消息接收者账号,此系统设计中为会话ID,通过会话ID寻找成员账号后群发", required = true)
        var receiver: String,
        @ApiModelProperty(value = "消息数据格式", dataType = "Enum")
        var format: String = Format.JSON.name,
        @ApiModelProperty(value = "附加内容")
        var extra: String? = null,
        @ApiModelProperty(value = "消息发送时间")
        var timestamp: Long = 0L
) : IProtobufAble {

    companion object {
        private const val serialVersionUID = 1L
    }

    init {
        timestamp = System.currentTimeMillis()
    }

    @JsonIgnore
    override val type: Byte = IMConstant.ProtobufType.MESSAGE

    override val byteArray: ByteArray
        @JsonIgnore
        @Throws(InvalidProtocolBufferException::class)
        get() {
            val builder = MessageProto.Model.newBuilder()
            builder.id = id
            builder.action = action
            builder.title = title ?: "新消息"
            builder.content = content
            builder.sender = sender
            builder.receiver = receiver
            builder.format = format
            builder.extra = extra ?: ""
            builder.timestamp = timestamp
            return builder.build().toByteArray()
        }

    override fun toString(): String {
        return "\n#Message#" +
                "\nid:" + id +
                "\naction:" + action +
                "\ntitle:" + title +
                "\ncontent:" + content +
                "\nextra:" + extra +
                "\nsender:" + sender +
                "\nreceiver:" + receiver +
                "\nformat:" + format +
                "\ntimestamp:" + timestamp
    }

    /**消息数据格式*/
    enum class Format {
        PROTOBUF, JSON, XML, TEXT;
    }
}