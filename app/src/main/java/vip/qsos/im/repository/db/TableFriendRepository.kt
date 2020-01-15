package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.model.db.TableFriend

@Repository
interface TableFriendRepository : JpaRepository<TableFriend, Long> {

    fun findByHashCode(hashCode: String): TableFriend?
    /**获取对此用户发起的好友申请列表*/
    fun findByFriendAndAccept(friend: Long, accept: Boolean? = null): List<TableFriend>

    /**获取用户的好友列表*/
    fun findByApplicantLikeOrFriendLikeAndAccept(applicant: Long, friend: Long, accept: Boolean = true): List<TableFriend>

}