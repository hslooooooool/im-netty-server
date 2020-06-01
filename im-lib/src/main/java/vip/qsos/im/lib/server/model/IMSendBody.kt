package vip.qsos.im.lib.server.model

import com.google.protobuf.InvalidProtocolBufferException
import vip.qsos.im.lib.model.proto.SendBodyProto
import vip.qsos.im.lib.server.config.IMConstant
import java.util.*

/**客户端发送的消息
 * @author : 华清松
 */
class IMSendBody : IProtobufAble {

    companion object {
        private const val serialVersionUID = 1L
    }

    /**请求key，即指令类型*/
    var key: String? = null

    /**发送时间*/
    var timestamp: Long = 0L

    /**发送数据集合*/
    private val data: Hashtable<String, String> = Hashtable()

    init {
        timestamp = System.currentTimeMillis()
    }

    constructor()
    constructor(key: String) {
        this.key = key
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

    override val type: Byte = IMConstant.ProtobufType.SEND_BODY

    override val byteArray: ByteArray
        @Throws(InvalidProtocolBufferException::class)
        get() {
            val builder = SendBodyProto.Model.newBuilder()
            builder.key = key
            builder.timestamp = timestamp
            if (data.isNotEmpty()) {
                builder.putAllData(data)
            }
            return builder.build().toByteArray()
        }

    override fun toString(): String {
        val buffer = StringBuffer()
        buffer.append("\n#SendBody#")
        buffer.append("\nkey:$key")
        buffer.append("\ntimestamp:").append(timestamp)
        buffer.append("\ndata{")
        if (!data.isEmpty) {
            for (key in keySet) {
                buffer.append("\t\t\n$key").append(":").append(this.find(key))
            }
        }
        buffer.append("\n}")
        return buffer.toString()
    }

}