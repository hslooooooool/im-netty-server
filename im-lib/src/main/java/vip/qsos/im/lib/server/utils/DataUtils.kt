package vip.qsos.im.lib.server.utils

/**数据处理工具
 * @author : 华清松
 */
object DataUtils {

    /**解析消息内容长度*/
    fun getContentLength(lv: Int, hv: Int): Int {
        val l = lv and  0xff
        val h = hv and 0xff
        return l or (h shl 8)
    }
}