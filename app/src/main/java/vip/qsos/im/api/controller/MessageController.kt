package vip.qsos.im.api.controller

import vip.qsos.im.api.controller.dto.MessageResult
import vip.qsos.im.push.MessagePusher
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.lib.server.model.Message
import javax.annotation.Resource

@RestController
@RequestMapping("/api/message")
class MessageController {
    @Resource
    private val messagePusher: MessagePusher? = null

    /**此方法仅仅在集群时，通过服务器调用 */
    @RequestMapping(value = ["/dispatch"], method = [RequestMethod.POST])
    fun dispatchSend(message: Message): MessageResult {
        return send(message)
    }

    @RequestMapping(value = ["/send"], method = [RequestMethod.POST])
    fun send(message: Message): MessageResult {
        val result = MessageResult()
        message.id = System.currentTimeMillis()
        messagePusher!!.push(message)
        result.id = message.id
        result.timestamp = message.timestamp
        return result
    }
}