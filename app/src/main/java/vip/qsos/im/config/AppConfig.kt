package vip.qsos.im.config

import com.google.gson.GsonBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.DateFormatter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.GsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*

@Configuration
open class AppConfig : WebMvcConfigurer {

    private val pattern = "yyyy-MM-dd HH:mm:ss"

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val httpMessageConverter = GsonHttpMessageConverter()
        val jsonConfig = GsonBuilder()
        jsonConfig.setDateFormat(pattern)
        converters.add(httpMessageConverter)
        super.configureMessageConverters(converters)
    }

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatterForFieldType(Date::class.java, DateFormatter(pattern))
    }
}
