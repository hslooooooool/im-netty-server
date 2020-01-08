package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.EnumSessionType

interface TableChatSessionRepository : JpaRepository<TableChatSession, Long> {

    fun findByMember(member: String): TableChatSession?
    fun findByMemberLike(member: String): List<TableChatSession>
    fun findBySessionTypeAndMember(sessionType: EnumSessionType, member: String): TableChatSession?

}