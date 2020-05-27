package vip.qsos.im.data_jpa.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.EnumSessionType

@Repository
interface TableChatSessionRepository : JpaRepository<TableChatSession, Long> {

    fun findByMember(member: String): TableChatSession?
    fun findByMemberLike(member: String): List<TableChatSession>
    fun findBySessionTypeAndMember(sessionType: EnumSessionType, member: String): TableChatSession?

}