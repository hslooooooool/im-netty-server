package vip.qsos.im.repository

import vip.qsos.im.model.db.TableChatSessionOfGroup

/**
 * @author : 华清松
 * 聊天群存储
 */
interface ISessionGroupRepository {
    fun create(name: String, memberList: List<String> = arrayListOf()): TableChatSessionOfGroup
    fun findByGroupId(groupId: Int): TableChatSessionOfGroup
    fun findByName(name: String, like: Boolean): List<TableChatSessionOfGroup>
    fun list(): List<TableChatSessionOfGroup>
    fun listLikeMember(member: String): List<TableChatSessionOfGroup>
    fun joinGroup(groupId: Int, member: String)
    fun leaveGroup(groupId: Int, member: String)
    fun deleteGroup(groupId: Int)
}