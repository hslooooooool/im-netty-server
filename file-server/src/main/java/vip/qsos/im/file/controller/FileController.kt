package vip.qsos.im.file.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import vip.qsos.im.file.entity.BaseResult
import vip.qsos.im.file.service.FileService
import javax.annotation.Resource
import javax.servlet.http.HttpServletResponse

@RestController
class FileController : FileApi {
    @Resource
    private lateinit var fileService: FileService

    override fun upLoad(file: MultipartFile): BaseResult {
        val result = fileService.upload(arrayListOf(file))
        return BaseResult.data(result[0])
    }

    override fun upLoads(request: MultipartHttpServletRequest): BaseResult {
        val files = request.multiFileMap["file"]
        if (files.isNullOrEmpty()) {
            throw Exception("文件上传不能为空")
        }
        val result = fileService.upload(files)
        return BaseResult.data(result)
    }

    override fun downLoad(httpResponse: HttpServletResponse, url: String) {
        fileService.downLoad(httpResponse, url)
    }

}
