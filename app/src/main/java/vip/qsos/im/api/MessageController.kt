package vip.qsos.im.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.BaseResult
import vip.qsos.im.push.MessagePusher
import javax.annotation.Resource

@RestController
@RequestMapping("/api/message")
class MessageController constructor(
        @Resource private val messagePusher: MessagePusher
) {

    /**此方法仅仅在集群时，通过服务器调用 */
    @PostMapping("/dispatch")
    fun dispatchSend(message: Message): BaseResult {
        return send(message)
    }

    @PostMapping("/send")
    fun send(message: Message): BaseResult {
        messagePusher.push(message)
        return BaseResult.data()
    }
}