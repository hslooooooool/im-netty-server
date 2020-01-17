package vip.qsos.im.config

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
open class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    open fun dataSource(): DataSource {
        return DruidDataSourceBuilder.create().build()
    }

}
