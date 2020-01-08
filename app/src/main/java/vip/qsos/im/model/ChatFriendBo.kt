package vip.qsos.im.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author : 华清松
 * 好友业务对象
 */
@ApiModel(value = "好友业务对象")
data class ChatFriendBo(
        @ApiModelProperty(value = "好友名称")
        var name: String,
        @ApiModelProperty(value = "在线状态")
        var status: Int = 0,
        @ApiModelProperty(value = "好友头像")
        var avatar: String? = null
)