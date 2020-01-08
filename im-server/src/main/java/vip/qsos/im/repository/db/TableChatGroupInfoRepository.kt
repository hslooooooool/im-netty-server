package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatGroupInfo

interface TableChatGroupInfoRepository : JpaRepository<TableChatGroupInfo, Long> {
    fun findByGroupId(groupId: Long): TableChatGroupInfo
}