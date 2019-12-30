package vip.qsos.im.model.type

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author : 华清松
 * 聊天类型
 * @sample ChatType.SINGLE 单聊
 * @sample ChatType.GROUP 群聊
 * @sample ChatType.NOTICE 公告
 * @sample ChatType.SUBSCRIPTION 订阅
 * @sample ChatType.SYSTEM 系统指令
 */
@ApiModel(value = "聊天类型")
enum class ChatType {
    @ApiModelProperty(value = "单聊")
    SINGLE,
    @ApiModelProperty(value = "群聊")
    GROUP,
    @ApiModelProperty(value = "公告")
    NOTICE,
    @ApiModelProperty(value = "订阅")
    SUBSCRIPTION,
    @ApiModelProperty(value = "系统指令")
    SYSTEM;

    companion object {
        fun getEnumByIndex(index: Int): ChatType {
            return values().find {
                it.ordinal == index
            }!!
        }
    }
}