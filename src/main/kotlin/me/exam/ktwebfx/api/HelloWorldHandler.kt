package me.exam.ktwebfx.api

import me.exam.ktwebfx.base.ApiResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class HelloWorldHandler {
    private val logger: Logger = LoggerFactory.getLogger(HelloWorldHandler::class.java)

    fun get(): Mono<ServerResponse> {
        logger.info("test")
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(ApiResult.ok("test"));

    }
}