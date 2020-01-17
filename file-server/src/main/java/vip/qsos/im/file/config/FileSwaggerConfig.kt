package vip.qsos.im.file.config

import io.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
open class FileSwaggerConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    @Bean(name = ["FILE-API"])
    open fun imApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .groupName("文件服务")
                .apiInfo(
                        ApiInfoBuilder().title("file")
                                .description("文件服务接口文档")
                                .version("1.0.0")
                                .contact(Contact("华清松", "https://github.com/hslooooooool", "821034742@qq.com"))
                                .build()
                )
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation::class.java))
                .paths(PathSelectors.ant("/api/file/**"))
                .build()
                .securitySchemes(security())
    }

    open fun security(): List<ApiKey> {
        return arrayListOf(ApiKey("token", "token", "header"))
    }

}
