package vip.qsos.im.model

/**
 * @author : 华清松
 * 基础返回对象
 */
open class BaseResult {
    var code = 200
    var msg: String? = null
    var data: Any? = null

    companion object {
        fun data(message: String = "success"): BaseResult {
            return BaseResult().also {
                it.code = 200
                it.msg = message
            }
        }

        fun data(data: Any?, message: String? = null): BaseResult {
            return BaseResult().also {
                it.code = 200
                it.data = data
                it.msg = message
            }
        }

        fun error(error: String, code: Int = 500): BaseResult {
            return BaseResult().also {
                it.msg = error
                it.code = code
            }
        }

        fun error(error: Exception, code: Int = 500): BaseResult {
            return BaseResult().also {
                it.msg = error.message
                it.code = code
            }
        }
    }
}