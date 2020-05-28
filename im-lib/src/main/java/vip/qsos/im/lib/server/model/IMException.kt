package vip.qsos.im.lib.server.model

/**消息服务异常
 * @author : 华清松
 */
class IMException : RuntimeException {

    var msg: String = "消息服务异常"
    var code = 500

    constructor(msg: String) : super(msg) {
        this.code = 500
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
