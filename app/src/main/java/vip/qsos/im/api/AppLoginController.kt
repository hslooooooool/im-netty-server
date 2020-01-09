package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.UserManageComponent
import vip.qsos.im.model.BaseResult
import javax.annotation.Resource

@RestController
class AppLoginController : AppLoginApi {

    @Resource
    private lateinit var mUserManageComponent: UserManageComponent

    override fun register(account: String, password: String): BaseResult {
        val user = mUserManageComponent.register(account, password)
        return BaseResult.data(user)
    }

    override fun login(account: String, password: String): BaseResult {
        val user = mUserManageComponent.login(account, password)
        return BaseResult.data(user)
    }

}