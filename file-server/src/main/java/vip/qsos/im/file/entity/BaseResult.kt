package vip.qsos.im.file.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author : 华清松
 * 基础返回对象
 */
@ApiModel(description = "统一返回对象")
open class BaseResult {
    @ApiModelProperty(value = "返回码")
    var code = 200
    @ApiModelProperty(value = "返回消息")
    var msg: String? = null
    @ApiModelProperty(value = "返回数据")
    var data: Any? = null

    companion object {
        fun data(message: String = "success"): BaseResult {
            return BaseResult().also {
                it.code = 200
                it.msg = message
            }
        }

        fun data(data: Any?, message: String = "success"): BaseResult {
            return BaseResult().also {
                it.code = 200
                it.data = data
                it.msg = message
            }
        }

        fun error(error: String): BaseResult {
            return BaseResult().also {
                it.msg = error
                it.code = 500
            }
        }

        fun error(code: Int, error: String): BaseResult {
            return BaseResult().also {
                it.msg = error
                it.code = code
            }
        }

        fun error(code: Int = 500, error: Exception): BaseResult {
            return BaseResult().also {
                it.msg = error.message
                it.code = code
            }
        }
    }
}