package vip.qsos.im.model.db

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.util.StringUtils
import vip.qsos.im.lib.server.model.ImException
import javax.persistence.*

@Entity
@Table(name = "table_chat_session_group")
@ApiModel(value = "客户端群组表")
data class TableChatSessionOfGroup constructor(
        @Id
        @Column(name = "id", length = 16)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "群号")
        var groupId: Int? = null,
        @Column(name = "name", nullable = false, length = 16)
        @ApiModelProperty(value = "群名称")
        var name: String = "",
        @Column(name = "member", nullable = false, length = 65530)
        @ApiModelProperty(value = "群成员账号集合，【规定】群成员不得超过【6553】人，" +
                "存储方式为成员9位账号 account 前追加一位 0/1 表在线状态")
        var member: String = ""
) : AbsTable() {

    companion object {
        /**将账号列表转为集合字符串*/
        fun addMember(memberList: List<String>): String {
            var member = ""
            memberList.forEach {
                member = member + "0" + it
            }
            return member
        }
    }

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
            str.length > 10 -> {
                val str1 = str.substring(0, 10)
                list.add(str1)
                getAccountList(str.substring(10), list)
            }
            str.length == 10 -> {
                list.add(str)
            }
            else -> {
                throw ImException("账号较验失败，长度不符合要求")
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