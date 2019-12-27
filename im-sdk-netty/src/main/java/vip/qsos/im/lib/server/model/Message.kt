package vip.qsos.im.lib.server.model

import com.google.protobuf.InvalidProtocolBufferException
import vip.qsos.im.lib.model.proto.MessageProto
import vip.qsos.im.lib.server.IMConstant

/**
 * @author : 华清松
 * 自定义消息对象
 */
class Message : IProtobufAble {

    companion object {
        private const val serialVersionUID = 1L
    }

    /**消息ID*/
    var id: Long = 0
    /**消息类型，自定义消息类型*/
    var action: String? = null
    /**消息标题*/
    var title: String? = null
    /**消息类容，content 根据 format 数据格式进行解析*/
    var content: String? = null
    /**消息发送者账号*/
    var sender: String? = null
    /**消息发送者接收者*/
    var receiver: String? = null
    /**content 内容格式，如 text,json,xml数据格式*/
    var format: String? = null
    /**附加内容*/
    var extra: String? = null
    /**消息发送时间*/
    var timestamp: Long = 0

    init {
        timestamp = System.currentTimeMillis()
    }

    override val type: Byte = IMConstant.ProtobufType.MESSAGE

    override val byteArray: ByteArray
        @Throws(InvalidProtocolBufferException::class)
        get() {
            val builder = MessageProto.Model.newBuilder()
            builder.id = id
            builder.action = action!!
            builder.title = title ?: "新消息"
            builder.content = content
            builder.sender = sender!!
            builder.receiver = receiver!!
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

}