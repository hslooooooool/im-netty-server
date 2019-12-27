package vip.qsos.im.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.BaseResult
import vip.qsos.im.component.MessagePusher
import javax.annotation.Resource

@RestController
@RequestMapping("/api/im.message")
class MessageController constructor(
        @Resource private val messagePusher: MessagePusher
) {
    @PostMapping("/send")
    fun send(message: Message): BaseResult {
        messagePusher.push(message)
        return BaseResult.data()
    }
}