package com.farsunset.cim

import com.farsunset.cim.config.AppProperties
import com.farsunset.cim.config.AppleProperties
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration
import org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

/**
 * @author : 华清松
 * ServerLauncher
 */
@SpringBootApplication(exclude = [
    WebSocketMessagingAutoConfiguration::class,
    WebSocketReactiveAutoConfiguration::class,
    WebSocketServletAutoConfiguration::class,
    WebServicesAutoConfiguration::class,
    JmxAutoConfiguration::class,
    DataSourceAutoConfiguration::class,
    ValidationAutoConfiguration::class,
    WebClientAutoConfiguration::class,
    JacksonAutoConfiguration::class]
)
@EnableConfigurationProperties(
        value = [
            AppProperties::class,
            AppleProperties::class
        ]
)
open class ServerLauncher

fun main(args: Array<String>) {
    SpringApplication.run(ServerLauncher::class.java, *args)
}