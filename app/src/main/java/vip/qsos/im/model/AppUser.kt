package vip.qsos.im.model

import io.swagger.annotations.ApiModelProperty

/**
 * @author : 华清松
 * APP用户
 */
data class AppUser(
        @ApiModelProperty(value = "用户ID")
        var userId: Long,
        @ApiModelProperty(value = "用户名称")
        var name: String = "",
        @ApiModelProperty(value = "关联的消息账号")
        var imAccount: String = "",
        @ApiModelProperty(value = "用户头像")
        var avatar: String? = null
)