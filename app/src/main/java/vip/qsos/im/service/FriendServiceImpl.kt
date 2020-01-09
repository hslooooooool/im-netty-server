package vip.qsos.im.service

import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.db.TableFriend
import vip.qsos.im.repository.db.TableFriendRepository
import javax.annotation.Resource

@Service
class FriendServiceImpl : FriendService {
    @Resource
    private lateinit var mTableFriendRepository: TableFriendRepository

    override fun addFriend(userId: Long, friendId: Long): TableFriend {
        var hashCode = ""
        arrayListOf(userId, friendId).sorted().forEach {
            hashCode += it
        }
        var mTableFriend = findFriend(userId, friendId)

        mTableFriend = mTableFriendRepository.saveAndFlush(TableFriend(
                id = mTableFriend?.id ?: -1L,
                applicant = userId, friend = friendId,
                hashCode = hashCode, accept = null
        ))
        return mTableFriend
    }

    override fun findFriend(userId: Long, friendId: Long): TableFriend? {
        var hashCode = ""
        arrayListOf(userId, friendId).sorted().forEach {
            hashCode += it
        }
        return mTableFriendRepository.findByHashCode(hashCode)
    }

    override fun findByFriendAndAccept(friendId: Long, accept: Boolean?): List<TableFriend> {
        return mTableFriendRepository.findByFriendAndAccept(friendId, accept)
    }

    override fun updateFriend(id: Long, accept: Boolean): TableFriend {
        try {
            return mTableFriendRepository.findById(id).get().let {
                it.accept = accept
                mTableFriendRepository.saveAndFlush(it)
            }
        } catch (e: Exception) {
            throw ImException("好友关系不存在")
        }
    }

    override fun findFriendList(userId: Long): List<TableFriend> {
        return mTableFriendRepository.findByApplicantLikeOrFriendLikeAndAccept(userId, userId)
    }

}