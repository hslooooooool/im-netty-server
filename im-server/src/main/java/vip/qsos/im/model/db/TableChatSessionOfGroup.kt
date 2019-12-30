package vip.qsos.im.model.db

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.util.StringUtils
import javax.persistence.*

@Entity
@Table(name = "table_chat_session_group")
@ApiModel(value = "客户端群组表")
data class TableChatSessionOfGroup constructor(
        @Id
        @Column(name = "id", length = 16)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "群号")
        var groupId: Long? = null,
        @Column(name = "name", nullable = false, length = 16)
        @ApiModelProperty(value = "群名称")
        var name: String = "",
        @Column(name = "state", nullable = false, length = 255)
        @ApiModelProperty(value = "群成员账号集合，【规定】群成员不得超过【55000】人，" +
                "存储方式为成员10位账号 account 前追加一位 0/1 表在线状态")
        var member: String = ""
) : AbsTable() {

    /**获取群内成员账号与对应状态*/
    @JsonIgnore
    fun getAccountList(): List<Account> {
        return getAccountList(member, arrayListOf()).map {
            val online = it.substring(0, 1) == "1"
            Account(it.substring(1), online)
        }
    }

    /**递归查询截取字符串*/
    @JsonIgnore
    private fun getAccountList(str: String, list: MutableList<String>): List<String> {
        if (StringUtils.isEmpty(str)) {
            return list
        }
        when {
            str.length > 11 -> {
                val str1 = str.substring(0, 11)
                list.add(str1)
                getAccountList(str.substring(11), list)
            }
            str.length == 11 -> {
                list.add(str)
            }
        }
        return list
    }

    /**成员账号
     * @param account 账号
     * @param online 在线
     */
    data class Account(var account: String, var online: Boolean)
}