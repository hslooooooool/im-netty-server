package vip.qsos.im.file.config

import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.Resource

/**
 * @author : 华清松
 * 文件路径映射
 */
@Configuration
open class FileWebConfig : WebMvcConfigurer {
    @Resource
    private lateinit var mProperties: MultipartProperties

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        super.addResourceHandlers(registry)
        // FIXME 映射文件访问路径
        registry.addResourceHandler("/${mProperties.location}/**")
                .addResourceLocations("file:${mProperties.location}/")
    }
}
