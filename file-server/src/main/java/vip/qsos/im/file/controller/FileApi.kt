package vip.qsos.im.file.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import vip.qsos.im.file.entity.BaseResult
import javax.servlet.http.HttpServletResponse

@Api(tags = ["文件接口"])
@RequestMapping("/api/file")
interface FileApi {

    @PostMapping("/upload/file", consumes = ["multipart/*"], headers = ["content-type=multipart/form-data"])
    @ApiOperation(value = "单文件上传")
    @ApiImplicitParams(
            ApiImplicitParam(name = "file", value = "单个文件，", paramType = "formData", allowMultiple = false, required = true, dataType = "file")
    )
    fun upLoad(@RequestParam("file") file: MultipartFile): BaseResult

    @PostMapping("/upload/files", consumes = ["multipart/*"], headers = ["content-type=multipart/form-data"])
    @ApiOperation(value = "多文件上传")
    @ApiImplicitParams(
            ApiImplicitParam(name = "file", value = "多个文件，", paramType = "formData", allowMultiple = true, required = true, dataType = "file")
    )
    fun upLoads(request: MultipartHttpServletRequest): BaseResult

    @GetMapping("/")
    @ApiOperation(value = "单文件下载")
    fun downLoad(httpResponse: HttpServletResponse, url: String)

}
