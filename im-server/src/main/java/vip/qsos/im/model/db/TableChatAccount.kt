package vip.qsos.im.model.db

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

@Entity
@Table(name = "table_chat_account")
@ApiModel(value = "聊天账号表")
data class TableChatAccount constructor(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "ID")
        var id: Int? = null,
        @Column(name = "account", unique = true, nullable = false, length = 9)
        @ApiModelProperty(value = "消息账号，固定长度9，由1-9组成的9位不重复字符串")
        var account: String = "",
        @ApiModelProperty(value = "是否被注册")
        @Column(name = "used")
        var used: Boolean = false
)