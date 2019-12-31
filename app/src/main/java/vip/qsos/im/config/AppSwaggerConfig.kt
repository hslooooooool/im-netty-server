package vip.qsos.im.config

import io.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
open class AppSwaggerConfig : WebMvcConfigurer {

    @Bean(name = ["APP-API"])
    open fun createRestApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .groupName("APP服务")
                .apiInfo(
                        ApiInfoBuilder().title("app")
                                .description("APP接口管理")
                                .version("1.0.0")
                                .contact(Contact("华清松", "https://github.com/hslooooooool", "821034742@qq.com"))
                                .license("Apache 2.0")
                                .build()
                )
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation::class.java))
                .paths(PathSelectors.ant("/api/app/**"))
                .build()
                .securitySchemes(arrayListOf(
                        ApiKey("token", "token", "header")
                ))
    }

}
