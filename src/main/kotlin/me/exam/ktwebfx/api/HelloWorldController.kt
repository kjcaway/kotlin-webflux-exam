package me.exam.ktwebfx.api

import me.exam.ktwebfx.api.dto.HelloDto
import me.exam.ktwebfx.api.service.HelloWorldService
import me.exam.ktwebfx.base.ApiResult
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RequestMapping("/api/hello")
@RestController
class HelloWorldController(
    val helloWorldService: HelloWorldService,
    //val jsonPropertyConfig: JsonPropertyConfig
) {
    @GetMapping()
    fun get(): Mono<ApiResult<*>> {
        return Mono.just(ApiResult.ok())
    }

//    @GetMapping("/json-env")
//    fun getJsonEnv(): Mono<ApiResult<*>> {
//        return Mono.just(ApiResult.ok(jsonPropertyConfig.secretKey))
//    }

    @GetMapping("/redis")
    fun getRedis(@RequestParam key: String, @RequestParam field: String): Mono<ApiResult<*>> {
        return helloWorldService.getHashValue(key, field)
            .map {
                ApiResult.ok(it)
            }
    }

    @PostMapping("/redis")
    fun postRedis(@RequestBody map: Map<*, *>): Mono<ApiResult<*>> {
        return helloWorldService.setHash(
            map["key"] as String,
            map["field"] as String,
            map["value"] as String
        )
            .map {
                ApiResult.ok()
            }
    }

    @GetMapping("/redis/all")
    fun getRedisAll(@RequestParam type: String, @RequestParam size: Long): Mono<ApiResult<*>> {
        return helloWorldService.getKeys(type, size)
            .collectList()
            .map {
                ApiResult.ok(it)
            }
    }

    @DeleteMapping("/redis/all")
    fun deleteRedisAll(): Mono<ApiResult<*>> {
        return helloWorldService.removeAll()
            .then(Mono.fromCallable { ApiResult.ok() })
    }

    @GetMapping("/redis/hellodto/{id}")
    fun getHelloDto(@PathVariable id: String): Mono<ApiResult<*>> {
        return helloWorldService.getJson(id)
            .map {
                ApiResult.ok(it)
            }
    }

    @PostMapping("/redis/hellodto")
    fun setHelloDto(@RequestBody helloDto: HelloDto): Mono<ApiResult<*>> {
        return helloWorldService.setJson(helloDto)
            .map {
                ApiResult.ok(it)
            }
    }
}