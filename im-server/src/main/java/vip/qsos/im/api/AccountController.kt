package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.model.BaseResult
import vip.qsos.im.repository.IAccountRepository
import javax.annotation.Resource

@RestController
class AccountController constructor(
        @Resource private val mAccountRepository: IAccountRepository
) : IAccountApi {

    override fun assign(): BaseResult {
        return BaseResult.data(data = mAccountRepository.assign())
    }

    override fun list(used: Boolean?): BaseResult {
        return BaseResult.data(data = mAccountRepository.list(used))
    }
}