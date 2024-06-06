package me.exam.ktwebfx.config

import me.exam.ktwebfx.api.HelloWorldHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class RouteConfig {

    @Bean
    fun router(helloWorldHandler: HelloWorldHandler): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .GET("/api/v2/hello") { helloWorldHandler.get() }
            .build()
    }
}