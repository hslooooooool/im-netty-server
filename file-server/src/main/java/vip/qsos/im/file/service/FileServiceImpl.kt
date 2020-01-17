package vip.qsos.im.file.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import vip.qsos.im.file.entity.FileResourceBo
import vip.qsos.im.file.repository.TableFileResourceRepository
import javax.annotation.Resource
import javax.servlet.http.HttpServletResponse

@Service
open class FileServiceImpl : FileService {

    @Resource
    private lateinit var mFileIOService: IFileIOService
    @Resource
    private lateinit var mFileResourceRepository: TableFileResourceRepository

    @Transactional(rollbackFor = [Exception::class])
    override fun upload(files: List<MultipartFile>): List<FileResourceBo> {
        val saveFiles: List<FileResourceBo>
        try {
            saveFiles = mFileIOService.saveData(files)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("文件上传失败")
        }
        val result = arrayListOf<FileResourceBo>()
        saveFiles.forEach {
            mFileResourceRepository.save(it.toTable())
            result.add(it)
        }
        return result
    }

    override fun downLoad(httpResponse: HttpServletResponse, url: String) {
        try {
            mFileIOService.getData(httpResponse, url)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("文件下载失败")
        }
    }
}
