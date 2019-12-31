package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.UserManageComponent
import vip.qsos.im.model.BaseResult
import javax.annotation.Resource

@RestController
class UserManageImpl : UserApi {

    @Resource
    private lateinit var mUserManageComponent: UserManageComponent

    override fun register(name: String, password: String): BaseResult {
        val user = mUserManageComponent.register(name, password)
        return BaseResult.data(user)
    }

    override fun login(name: String, password: String): BaseResult {
        val user = mUserManageComponent.login(name, password)
        return BaseResult.data(user)
    }
}