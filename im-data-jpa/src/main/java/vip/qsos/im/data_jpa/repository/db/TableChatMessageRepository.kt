package vip.qsos.im.data_jpa.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.data_jpa.model.table.TableChatMessage

@Repository
interface TableChatMessageRepository : JpaRepository<TableChatMessage, Long>