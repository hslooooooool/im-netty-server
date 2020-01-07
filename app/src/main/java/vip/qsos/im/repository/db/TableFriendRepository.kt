package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableFriend

interface TableFriendRepository : JpaRepository<TableFriend, Long> {

    fun findByHashCode(hashCode: String): TableFriend?
    /**获取对此用户发起的好友申请列表*/
    fun findByFriendAndAccept(friend: Long, accept: Boolean? = null): List<TableFriend>

}