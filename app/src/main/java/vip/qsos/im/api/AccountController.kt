package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.service.IMAccountService
import javax.annotation.Resource

@RestController
class AccountController constructor(
        @Resource private val mIMAccountService: IMAccountService
) : AccountApi {

    override fun assign(): BaseResult {
        return BaseResult.data(data = mIMAccountService.assign())
    }

    override fun list(used: Boolean?): BaseResult {
        return BaseResult.data(data = mIMAccountService.list(used))
    }

    override fun init(size: Int): BaseResult {
        if (size > 100) {
            throw IMException("每次最多生成100个账号")
        }
        return BaseResult.data(data = mIMAccountService.create(size))
    }
}