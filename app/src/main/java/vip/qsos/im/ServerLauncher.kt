package vip.qsos.im

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author : 华清松
 * ServerLauncher
 */
@SpringBootApplication
open class ServerLauncher

fun main(args: Array<String>) {
    SpringApplication.run(ServerLauncher::class.java, *args)
}