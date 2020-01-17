package vip.qsos.im.file.service

import net.coobird.thumbnailator.Thumbnails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import vip.qsos.im.file.VideoImageHelper
import vip.qsos.im.file.entity.FileResourceBo
import java.awt.image.BufferedImage
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletResponse

/**
 * @author : 华清松
 * @date : 2018/12/20
 * @description : 文件保存处理
 */
@Service
open class FileIOServiceImpl @Autowired constructor(
        private val mProperties: MultipartProperties
) : IFileIOService {

    /**下载资源*/
    override fun getData(httpResponse: HttpServletResponse, url: String) {
        var fileInputStream: FileInputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileName = url.substring(url.lastIndexOf(File.separator) + 1)
            httpResponse.characterEncoding = "utf-8"
            httpResponse.contentType = "application/x-msdownload"
            // 设置文件名
            httpResponse.setHeader("Content-Disposition", "attachment;filename=$fileName")
            fileInputStream = FileInputStream(url)
            outputStream = httpResponse.outputStream
            val data = ByteArray(1024)
            var len: Int = fileInputStream.read(data)
            while (len != -1) {
                len = fileInputStream.read(data)
                outputStream!!.write(data, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
            outputStream?.close()
        }
    }

    /**保存资源*/
    override fun saveData(multipartFile: List<MultipartFile>): List<FileResourceBo> {
        val fileList = ArrayList<FileResourceBo>()
        var outputStream: FileOutputStream?
        var inputStream: InputStream?
        for (file in multipartFile) {
            val dateFolder = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val folderPath = "${mProperties.location}/$dateFolder/"
            val folder = File(folderPath)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val fileInfo = getUUIDFileName(file)
            val uuidFileName = fileInfo[0] + fileInfo[1]
            val uuidFileNameOfAvatar = fileInfo[0] + "_avatar.png"
            val originalUrl = "$folderPath$uuidFileName"
            var originalUrlOfAvatar = "$folderPath$uuidFileNameOfAvatar"

            inputStream = file.inputStream
            outputStream = FileOutputStream(originalUrl)
            val data = ByteArray(1024)
            var len = 0
            while (len != -1) {
                len = inputStream.read(data)
                if (len != -1) {
                    outputStream.write(data, 0, len)
                }
            }
            inputStream.close()
            outputStream.close()
            when (fileInfo[1].toLowerCase()) {
                ".jpg", ".jpeg", ".png", ".gif", ".bmp" -> {
                    // 如果是图片，保存一份缩略图
                    originalUrlOfAvatar = changPicture(originalUrl, originalUrlOfAvatar)
                }
                ".mp4", ".3gp", ".flv" -> {
                    VideoImageHelper.randomGrabberFFmpegImage(originalUrl, 0.1f, originalUrlOfAvatar)
                }
                else -> {
                }
            }

            fileList.add(FileResourceBo(
                    avatar = originalUrlOfAvatar,
                    url = originalUrl,
                    filename = uuidFileName,
                    type = fileInfo[1]
            ))
        }
        return fileList
    }

    /**将原图压缩成256*256*/
    private fun changPicture(originalUrl: String, changeUrl: String): String {
        try {
            val file = File(originalUrl)
            val image = ImageIO.read(file)
            return if (file.length() <= 100 * 1000 || image.width <= 256 || image.height <= 256) {
                originalUrl
            } else {
                Thumbnails.of(originalUrl)
                        .size(256, 256)
                        .imageType(BufferedImage.TYPE_INT_ARGB)
                        .outputFormat("png")
                        .toFile(changeUrl)

                changeUrl
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return try {
                val thumbnail = Thumbnails.of(originalUrl)
                        .size(256, 256)
                        .imageType(BufferedImage.TYPE_INT_ARGB)
                        .asBufferedImage()
                ImageIO.write(thumbnail, "png", File(changeUrl))

                changeUrl
            } catch (e: Exception) {
                e.printStackTrace()

                originalUrl
            }
        }
    }

    /**获得拼接的uuid*/
    @Throws(Exception::class)
    private fun getUUIDFileName(file: MultipartFile): Array<String> {
        // 生成uuid
        val uuid = UUID.randomUUID().toString().replace("-".toRegex(), "")
        // 文件名后缀
        val suffix = file.originalFilename!!.substring(file.originalFilename!!.lastIndexOf("."))
        return arrayOf(uuid, suffix)
    }
}
