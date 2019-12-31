package vip.qsos.im.config

import org.springframework.dao.DuplicateKeyException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import vip.qsos.im.model.AppException
import vip.qsos.im.model.BaseResult

/**
 * @author : 华清松
 * 服务异常处理
 */
@RestControllerAdvice
class AppExceptionHandler {

    @ExceptionHandler(AppException::class)
    fun handleRRException(e: AppException): BaseResult {
        e.printStackTrace()
        return BaseResult.error(e.code, e.message ?: "服务器异常：${e.message}")
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(e: DuplicateKeyException): BaseResult {
        e.printStackTrace()
        return BaseResult.error("数据错误")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): BaseResult {
        e.printStackTrace()
        return BaseResult.error("请求失败，${e.message}")
    }
}
