package me.exam.ktwebfx.api

import me.exam.ktwebfx.base.ApiResult
import me.exam.ktwebfx.config.JsonPropertyConfig
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RequestMapping("/api/hello")
@RestController
class HelloWorldController(
        val helloWorldService: HelloWorldService,
        val jsonPropertyConfig: JsonPropertyConfig
) {
    @GetMapping()
    fun get(): Mono<ApiResult<*>> {
        return Mono.just(ApiResult.ok())
    }

    @GetMapping("/json-env")
    fun getJsonEnv(): Mono<ApiResult<*>> {

        return Mono.just(ApiResult.ok(jsonPropertyConfig.secretKey))
    }
}