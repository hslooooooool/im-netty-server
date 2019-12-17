package com.farsunset.cim.api.controller.dto

/**
 * @author : 华清松
 * 基础返回对象
 */
open class BaseResult {
    var code = 200
    var message: String? = null
    var data: Any? = null
    var dataList: List<*>? = null
}