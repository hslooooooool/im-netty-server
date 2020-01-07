package vip.qsos.im.service

/**
 * @author : 华清松
 * 服务基本接口
 * @param I id类型
 * @param T 结果表
 */
interface AbsService<I, T> {
    fun findById(id: I): T
}