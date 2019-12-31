package vip.qsos.im.lib.server.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.protobuf.InvalidProtocolBufferException
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.model.proto.MessageProto
import vip.qsos.im.lib.server.config.IMConstant

/**
 * @author : 华清松
 * 自定义消息对象
 */
@ApiModel(description = "消息实体")
data class Message constructor(
        @ApiModelProperty(value = "消息ID")
        var id: Long? = null,
        @ApiModelProperty(value = "消息类型，自定义消息类型，如0:文本、1:文件等")
        var action: String = "0",
        @ApiModelProperty(value = "消息标题")
        var title: String? = null,
        @ApiModelProperty(value = "消息内容，content 根据 format 数据格式进行解析", required = true)
        var content: String,
        @ApiModelProperty(value = "消息发送者账号", required = true)
        var sender: String,
        @ApiModelProperty(value = "消息接收者账号", required = true)
        var receiver: String,
        @ApiModelProperty(value = "消息数据格式 protobuf text json xml")
        var format: String = Format.PROTOBUF.value,
        @ApiModelProperty(value = "附加内容")
        var extra: String? = null,
        @ApiModelProperty(value = "消息发送时间")
        var timestamp: Long = 0
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
            builder.id = id ?: -1L
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

    /**消息数据格式 protobuf text json xml*/
    enum class Format(val value: String) {
        PROTOBUF("protobuf"),
        JSON("json"),
        XML("xml"),
        TEXT("text");
    }
}