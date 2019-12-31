package vip.qsos.im.config

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.BaseResult

/**
 * @author : 华清松
 * 消息服务异常处理
 */
@RestControllerAdvice
class ImExceptionHandler {

    @ExceptionHandler(ImException::class)
    fun handleRRException(e: ImException): BaseResult {
        e.printStackTrace()
        return BaseResult.error(e.code, e.message ?: "服务器异常：${e.message}")
    }

}
