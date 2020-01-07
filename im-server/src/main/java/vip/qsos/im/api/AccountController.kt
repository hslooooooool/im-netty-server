package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.service.ChatAccountRepository
import javax.annotation.Resource

@RestController
class AccountController constructor(
        @Resource private val mChatAccountRepository: ChatAccountRepository
) : AccountApi {

    override fun assign(): BaseResult {
        return BaseResult.data(data = mChatAccountRepository.assign())
    }

    override fun list(used: Boolean?): BaseResult {
        return BaseResult.data(data = mChatAccountRepository.list(used))
    }

    override fun init(size: Int): BaseResult {
        if (size > 100) {
            throw ImException("每次最多生成100个账号")
        }
        return BaseResult.data(data = mChatAccountRepository.init(size))
    }
}