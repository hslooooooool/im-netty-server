package vip.qsos.im.model.db

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass

/**
 * @author : 华清松
 * @description : 数据库表通用字段
 */
@MappedSuperclass
abstract class AbsTable(
        @Column(name = "gmt_create", nullable = false)
        var gmtCreate: LocalDateTime = LocalDateTime.now(),

        @Column(name = "gmt_update", nullable = false)
        var gmtUpdate: LocalDateTime = LocalDateTime.now(),

        @Column(name = "deleted", nullable = false)
        var deleted: Boolean = false
)