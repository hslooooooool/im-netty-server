package vip.qsos.im.service

import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.ChatMessageBo
import vip.qsos.im.model.db.AbsTableChatMessage

/**
 * @author 华清松
 * 单聊服务
 */
interface SessionOfSingleService {

    /**发送单聊消息
     * @param sessionId 会话ID
     * @param contentType 消息类型
     * @param content 消息内容
     * @param sender 发送人消息账号
     * @return 发送后的消息
     * */
    fun sendMessage(sessionId: Long, contentType: Int, content: String, sender: String): AbsTableChatMessage

    /**获取消息列表
     * @param sessionId 会话ID
     * @param timeline 查询起始时序
     * @param size 查询条数
     * @param previous 是否从起始时序向上查询
     * @return 消息列表
     * */
    fun getMessageListByTimeline(sessionId: Long, timeline: Long, size: Int = 10, previous: Boolean = true): List<ChatMessageBo>

}