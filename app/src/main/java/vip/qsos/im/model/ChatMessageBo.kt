package vip.qsos.im.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.Message

/**
 * @author : 华清松
 * 消息业务对象
 */
@ApiModel(value = "消息业务对象")
data class ChatMessageBo(
        @ApiModelProperty(value = "消息发送人")
        var user: AppUserBo,
        @ApiModelProperty(value = "消息对象")
        var message: Message
)