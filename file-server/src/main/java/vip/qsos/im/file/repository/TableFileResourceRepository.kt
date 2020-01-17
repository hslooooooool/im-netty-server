package vip.qsos.im.file.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.file.entity.TableFileResource

@Repository
interface TableFileResourceRepository : JpaRepository<TableFileResource, Long>