package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatGroupOfLastRecord

interface IChatGroupOfLastRecordRepository : JpaRepository<TableChatGroupOfLastRecord, Int>