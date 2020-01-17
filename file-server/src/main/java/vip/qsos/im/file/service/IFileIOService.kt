package vip.qsos.im.file.service

import org.springframework.web.multipart.MultipartFile
import vip.qsos.im.file.entity.FileResourceBo
import javax.servlet.http.HttpServletResponse

/**
 * @author : 华清松
 * @description : 文件数据IO操作
 */
interface IFileIOService {

    /**下载资源*/
    @Throws(Exception::class)
    fun getData(httpResponse: HttpServletResponse, url: String)

    /**保存资源*/
    @Throws(Exception::class)
    fun saveData(multipartFile: List<MultipartFile>): List<FileResourceBo>

}