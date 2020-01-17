package vip.qsos.im.file.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "file_resource")
class TableFileResource : AbsTable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var fileId: Long = -1L

    @Column(name = "avatar")
    var avatar: String? = null

    @Column(name = "file_url")
    var url: String? = null

    @Column(name = "file_name")
    var filename: String? = null

    @Column(name = "file_type")
    var type: String? = null

    constructor()
    constructor(
            fileId: Long = -1L,
            avatar: String?,
            url: String?,
            filename: String?,
            type: String?,

            gmtCreate: LocalDateTime = LocalDateTime.now(),
            gmtUpdate: LocalDateTime = LocalDateTime.now(),
            deleted: Boolean = false
    ) {
        this.fileId = fileId
        this.avatar = avatar
        this.url = url
        this.filename = filename
        this.type = type

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}