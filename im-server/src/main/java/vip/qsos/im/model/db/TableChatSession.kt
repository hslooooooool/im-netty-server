package vip.qsos.im.model.db

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.util.StringUtils
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.type.EnumSessionType
import javax.persistence.*

@Entity
@Table(name = "table_chat_session")
@ApiModel(value = "聊天会话表", description = "每一个建立了聊天关系的集群，都将有一条数据，" +
        "与群聊群、公众群（号）、单聊群、系统公告等一一对应，根据会话类型识别")
data class TableChatSession constructor(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(value = "会话ID")
        var sessionId: Long = -1L,
        @Column(name = "creator", nullable = false, length = 9)
        @ApiModelProperty(value = "创建者IM账号")
        var creator: String = "",
        @Column(name = "session_type", nullable = false, length = 20)
        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "会话类型，默认 SINGLE 单聊")
        var sessionType: EnumSessionType = EnumSessionType.SINGLE,
        @Column(name = "member", nullable = false, length = 65530, columnDefinition = "TEXT")
        @ApiModelProperty(value = "群成员账号集合，【规定】群成员不得超过【6553】人，" +
                "存储方式为成员9位账号 account 前追加一位 0/1 表是离群状态")
        var member: String = ""
) : AbsTable() {

    companion object {
        /**将账号列表转为集合字符串*/
        fun addMember(memberList: List<String>): String {
            var member = ""
            memberList.toSet().sorted().forEach {
                member = member + "0" + it
            }
            return member
        }
    }

    /**获取群内成员账号与对应状态*/
    @JsonIgnore
    fun getAccount(account: String): Account? {
        var mAccount: Account? = null
        val accounts = getAccountList()
        for (it in accounts) {
            if (it.account == account) {
                mAccount = it
                break
            }
        }
        return mAccount
    }

    /**获取群内成员账号与对应状态*/
    @JsonIgnore
    fun getAccountList(): List<Account> {
        return getAccountList(member, arrayListOf()).map {
            val leave = it.substring(0, 1) == "1"
            Account(it.substring(1), leave)
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
     * @param leave 离群状态
     */
    data class Account(var account: String, var leave: Boolean)
}