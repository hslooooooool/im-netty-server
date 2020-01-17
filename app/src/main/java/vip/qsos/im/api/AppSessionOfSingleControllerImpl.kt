package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.model.BaseResult
import vip.qsos.im.service.SessionOfSingleService
import javax.annotation.Resource

@RestController
class AppSessionOfSingleControllerImpl : AppSessionOfSingleApi {
    @Resource
    private lateinit var mSessionOfSingleService: SessionOfSingleService

    override fun sendMessage(sessionId: Long, type: Int, data: String, sender: String): BaseResult {
        val message = mSessionOfSingleService.sendMessage(sessionId, type, data, sender)
        return BaseResult.data(message, "发送成功")
    }

    override fun getMessageListByTimeline(sessionId: Long, timeline: Long, size: Int, previous: Boolean): BaseResult {
        val messages = mSessionOfSingleService.getMessageListByTimeline(sessionId, timeline, size, previous)
        return BaseResult.data(messages)
    }
}