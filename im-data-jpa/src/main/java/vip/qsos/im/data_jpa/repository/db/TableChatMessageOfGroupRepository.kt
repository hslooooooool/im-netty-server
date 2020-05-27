package vip.qsos.im.data_jpa.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.model.db.TableChatMessageOfGroup

@Repository
interface TableChatMessageOfGroupRepository : JpaRepository<TableChatMessageOfGroup, Long>