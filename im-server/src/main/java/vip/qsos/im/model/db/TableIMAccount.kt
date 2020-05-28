package vip.qsos.im.model.db

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.im.lib.server.model.IMAccount
import javax.persistence.*

@Entity
@Table(name = "table_im_account")
@ApiModel(value = "消息服务账号表")
data class TableIMAccount constructor(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "ID")
        override var id: Int = -1,
        @Column(name = "account", unique = true, nullable = false, length = 9)
        @ApiModelProperty(value = "消息账号，固定长度9，由1-9组成的9位不重复字符串")
        override var account: String = "",
        @ApiModelProperty(value = "是否被注册")
        @Column(name = "used")
        override var used: Boolean = false
) : IMAccount