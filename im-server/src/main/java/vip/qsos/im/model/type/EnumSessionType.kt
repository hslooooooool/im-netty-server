package vip.qsos.im.model.type

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author : 华清松
 * 聊天类型
 * @sample EnumSessionType.SINGLE 单聊
 * @sample EnumSessionType.GROUP 群聊
 * @sample EnumSessionType.NOTICE 公告
 * @sample EnumSessionType.SUBSCRIPTION 订阅
 * @sample EnumSessionType.SYSTEM 系统指令
 */
@ApiModel(value = "会话类型")
enum class EnumSessionType {
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
}