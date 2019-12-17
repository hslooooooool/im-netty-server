package vip.qsos.im.lib.server.model

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : 全局异常
 */
class ImException : RuntimeException {

    var msg: String? = null
    var code = 500

    constructor(msg: String) : super(msg) {
        this.msg = msg
    }

    constructor(msg: String, e: Throwable) : super(msg, e) {
        this.msg = msg
    }

    constructor(msg: String, code: Int) : super(msg) {
        this.msg = msg
        this.code = code
    }

}
