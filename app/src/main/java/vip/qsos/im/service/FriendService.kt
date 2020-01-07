package vip.qsos.im.service

import vip.qsos.im.model.db.TableFriend
import vip.qsos.im.model.db.TableUser

/**
 * @author : 华清松
 * 好友关系管理服务
 */
interface FriendService {

    fun addFriend(userId: Long, friendId: Long): TableFriend
    fun findFriend(userId: Long, friendId: Long): TableFriend?
    fun findByFriendAndAccept(friendId: Long, accept: Boolean?): List<TableFriend>
    fun updateFriend(id: Long, accept: Boolean): TableFriend

}