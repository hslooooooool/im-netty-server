package vip.qsos.im.file.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "文件对象")
class FileResourceBo {
    @ApiModelProperty(name = "avatar", value = "文件封面")
    var avatar: String? = null
    @ApiModelProperty(name = "url", value = "文件链接")
    var url: String? = null
    @ApiModelProperty(name = "filename", value = "文件名称")
    var filename: String? = null
    @ApiModelProperty(name = "type", value = "文件类型")
    var type: String? = null

    constructor()
    constructor(avatar: String? = null, url: String? = null, filename: String? = null, type: String? = null) {
        this.avatar = avatar
        this.url = url
        this.filename = filename
        this.type = type
    }

    fun toTable(): TableFileResource {
        return TableFileResource(
                avatar = avatar,
                url = url,
                filename = filename,
                type = type
        )
    }

    companion object {
        // FIXME 配置访问HOST
        const val PATH = "http://192.168.2.103:8085/"
    }

    fun getBo(table: TableFileResource?): FileResourceBo? {
        return table?.let {
            FileResourceBo(
                    avatar = PATH + table.avatar,
                    url = PATH + table.url,
                    filename = table.filename,
                    type = table.type
            )
        }
    }
}