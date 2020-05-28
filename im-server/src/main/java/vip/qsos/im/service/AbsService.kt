package vip.qsos.im.service

/**服务基本接口
 * @author : 华清松
 * @param I id类型
 * @param T 结果表
 */
interface AbsService<I, T> {
    /**通过主键 id 查询
     * @param id 主键 id
     * */
    fun findById(id: I): T
}