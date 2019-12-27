package vip.qsos.im.lib.server.model

/**
 * @author : 华清松
 * 消息服务异常
 */
class ImException : RuntimeException {

    var msg: String = "消息服务异常"
    var code = 500

    constructor(msg: String) : super(msg) {
        this.msg = msg
    }

    constructor(error: Exception) : super(error) {
        this.msg = error.message ?: "未知异常"
    }

    constructor(msg: String, code: Int) : super(msg) {
        this.msg = msg
        this.code = code
    }

    constructor(msg: String, e: Throwable) : super(msg, e) {
        this.msg = msg + e.message
    }

}