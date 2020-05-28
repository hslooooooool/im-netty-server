package vip.qsos.im.lib.server.model

import java.io.Serializable

/**Protobuf消息体接口
 * @author : 华清松
 */
interface IProtobufAble : Serializable {

    /**消息数据*/
    val byteArray: ByteArray

    /**消息类型*/
    val type: Byte
}
