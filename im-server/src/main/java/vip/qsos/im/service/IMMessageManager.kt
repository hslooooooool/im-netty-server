package vip.qsos.im.service

import vip.qsos.im.lib.server.model.IMMessage
import vip.qsos.im.model.type.EnumSessionType

/**消息管理接口*/
interface IMMessageManager {
    /**保存消息
     * @param sessionId 会话 id
     * @param sessionType 会话类型
     * @param message 消息内容*/
    fun save(sessionId: Long, sessionType: EnumSessionType, message: IMMessage): IMMessage

    /**查询消息
     * @param sessionType 会话类型
     * @param messageId 消息 id*/
    fun find(sessionType: EnumSessionType, messageId: Long): IMMessage?

    /**删除消息
     * @param sessionType 会话类型
     * @param messageId 消息 id*/
    fun remove(sessionType: EnumSessionType, messageId: Long)

    /**查询消息列表
     * @param sessionId 会话 id
     * @param timeline 时间线
     * @param previous 时间线方向，true往前，false往后*/
    fun list(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<IMMessage>

}