package vip.qsos.im.lib.server.model

import com.google.protobuf.InvalidProtocolBufferException
import vip.qsos.im.lib.model.proto.ReplyBodyProto
import vip.qsos.im.lib.server.config.IMConstant
import java.util.*

/**
 * @author : 华清松
 * 服务器应答对象，客户端主动请求服务器后收到的回执消息
 */
class ReplyBody : IProtobufAble {

    companion object {
        private const val serialVersionUID = 1L
    }

    /**请求key*/
    var key: String? = null
    /**返回码*/
    var code: String = "200"
    /**返回说明*/
    var message: String? = null
    /**返回时间*/
    var timestamp: Long = 0L
    /**返回数据集合*/
    private val data: Hashtable<String, String> = Hashtable()

    init {
        timestamp = System.currentTimeMillis()
    }

    private val keySet: Set<String>
        get() = data.keys

    fun find(k: String): String? {
        return data[k]
    }

    fun put(k: String?, v: String?) {
        if (k == null || v == null) {
            return
        }
        data[k] = v
    }

    fun remove(k: String) {
        data.remove(k)
    }

    fun putAll(map: Map<String, String>) {
        data.putAll(map)
    }

    override val type: Byte = IMConstant.ProtobufType.REPLY_BODY

    override val byteArray: ByteArray
        @Throws(InvalidProtocolBufferException::class)
        get() {
            val builder = ReplyBodyProto.Model.newBuilder()
            builder.key = key
            builder.code = code
            builder.timestamp = timestamp
            message?.let { builder.message = message }
            if (data.isNotEmpty()) {
                builder.putAllData(data)
            }
            return builder.build().toByteArray()
        }

    override fun toString(): String {
        val buffer = StringBuffer()
        buffer.append("\n#ReplyBody#")
        buffer.append("\nkey:$key")
        buffer.append("\ntimestamp:$timestamp")
        buffer.append("\ncode:$code")
        buffer.append("\ndata{")
        if (!data.isEmpty) {
            for (key in keySet) {
                buffer.append("\t\t\n$key").append(":").append(find(key))
            }
        }
        buffer.append("\n}")
        return buffer.toString()
    }
}