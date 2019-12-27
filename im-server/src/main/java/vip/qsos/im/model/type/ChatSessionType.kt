package vip.qsos.im.model.type

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author : 华清松
 * 会话类型
 * @sample ChatSessionType.SINGLE 单聊
 * @sample ChatSessionType.GROUP 群聊
 * @sample ChatSessionType.NOTICE 公告
 * @sample ChatSessionType.SUBSCRIPTION 订阅
 */
@ApiModel(value = "会话类型")
enum class ChatSessionType {
    @ApiModelProperty(value = "单聊")
    SINGLE,
    @ApiModelProperty(value = "群聊")
    GROUP,
    @ApiModelProperty(value = "公告")
    NOTICE,
    @ApiModelProperty(value = "订阅")
    SUBSCRIPTION;

    companion object {
        fun getEnumByIndex(index: Int): ChatSessionType {
            return values().find {
                it.ordinal == index
            }!!
        }
    }
}