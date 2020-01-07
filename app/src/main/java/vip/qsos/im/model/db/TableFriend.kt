package vip.qsos.im.model.db

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

/**
 * @author : 华清松
 * 好友关系表
 */
@Entity
@Table(name = "table_friend", indexes = [
    Index(name = "hash_code", columnList = "hash_code", unique = true)
])
@ApiModel(value = "好友关系表")
data class TableFriend constructor(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        @ApiModelProperty(value = "关系ID")
        var id: Long = -1L,
        @Column(name = "applicant", nullable = false)
        @ApiModelProperty(value = "申请用户ID")
        var applicant: Long = -1L,
        @Column(name = "friend", nullable = false)
        @ApiModelProperty(value = "好友用户ID")
        var friend:  Long = -1L,
        @Column(name = "accept")
        @ApiModelProperty(value = "是否接受")
        var accept: Boolean? = null,
        @Column(name = "hash_code", unique = true, nullable = false)
        @ApiModelProperty(value = "好友关系hash值，双方账号正序排序并拼接后的值，保证每组好友关系唯一")
        var hashCode: String = ""
) : AbsTable()