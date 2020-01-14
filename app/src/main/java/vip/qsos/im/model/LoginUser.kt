package vip.qsos.im.model

import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.model.db.TableUser

/**
 * @author : 华清松
 * 登录用户
 */
data class LoginUser(
        @ApiModelProperty(value = "用户ID")
        var id: Long,
        @ApiModelProperty(value = "姓名")
        var name: String = "",
        @ApiModelProperty(value = "密码")
        var password: String = "",
        @ApiModelProperty(value = "关联的消息账号")
        var imAccount: String = "",
        @ApiModelProperty(value = "用户头像")
        var avatar: String? = null
) {
    companion object {
        fun getBo(table: TableUser): LoginUser {
            return LoginUser(table.userId, table.name, table.password, table.imAccount, table.avatar)
        }
    }
}