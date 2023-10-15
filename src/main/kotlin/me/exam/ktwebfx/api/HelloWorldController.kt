package me.exam.ktwebfx.api

import me.exam.ktwebfx.base.ApiResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RequestMapping("/api/hello")
@RestController
class HelloWorldController(
        val helloWorldService: HelloWorldService
) {
    @GetMapping()
    fun get(): Mono<ApiResult<*>> {
        return Mono.just(ApiResult.ok())
    }
}