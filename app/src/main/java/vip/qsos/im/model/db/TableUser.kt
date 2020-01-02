package vip.qsos.im.model.db

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

/**
 * @author : 华清松
 * 用户表
 */
@Entity
@Table(
        name = "table_user",
        indexes = [
            Index(name = "name", columnList = "name", unique = true)
        ]
)
@ApiModel(value = "用户表")
data class TableUser(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        @ApiModelProperty(value = "用户ID")
        var userId: Long = -1L,
        @Column(name = "name", unique = true, nullable = false, updatable = false, length = 16)
        @ApiModelProperty(value = "用户名称")
        var name: String = "",
        @Column(name = "password", nullable = false, length = 16)
        @ApiModelProperty(value = "密码")
        var password: String = "",
        @Column(name = "im_account", unique = true, nullable = false, length = 9)
        @ApiModelProperty(value = "关联的消息账号")
        var imAccount: String = "",
        @Column(name = "avatar")
        @ApiModelProperty(value = "用户头像")
        var avatar: String? = null
) : AbsTable()