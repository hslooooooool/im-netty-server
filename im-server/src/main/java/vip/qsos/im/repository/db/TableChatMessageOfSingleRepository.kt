package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatMessageOfSingle

interface TableChatMessageOfSingleRepository : JpaRepository<TableChatMessageOfSingle, Long>