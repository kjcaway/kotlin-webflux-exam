package me.exam.ktwebfx.api.service

import me.exam.ktwebfx.api.dto.HelloDto
import me.exam.ktwebfx.error.CustomErrorCode
import me.exam.ktwebfx.error.CustomException
import me.exam.ktwebfx.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.UUID

@Service
class HelloWorldService(
        val redisTemplate: ReactiveRedisTemplate<String, Any>,
        val redisOperations: ReactiveRedisOperations<String, Any>

) {
    private val logger: Logger = LoggerFactory.getLogger(HelloWorldService::class.java)

    private final val REDIS_PREFIX = "ktwebfx"
    private final val TYPE_HASH = "hash"
    private final val TYPE_JSONSTR = "jsonstr"

    fun getKeys(type: String, size: Long): Flux<String> {
        val options = ScanOptions.scanOptions()
                .match("${REDIS_PREFIX}::${type}::*")
                .count(size)
                .build()

        return redisOperations.scan(options)
    }

    fun setHash(key: String, field: String, value: String): Mono<Boolean> {
        val hashKey = "${REDIS_PREFIX}::${TYPE_HASH}::${key}"

        return redisTemplate.opsForHash<String, String>().put(hashKey, field, value)
                .doOnSuccess { logger.info("set hash $key $field $value") }
                .doOnError { logger.error("failed to set hash $key $field $value", it) }
    }

    fun getHashValue(key: String, field: String): Mono<String?> {
        val hashKey = "${REDIS_PREFIX}::${TYPE_HASH}::${key}"

        return redisTemplate.opsForHash<String, String>().get(hashKey, field)
                .switchIfEmpty {
                    Mono.error(CustomException(CustomErrorCode.NOT_FOUND))
                }
                .mapNotNull { value ->
                    value
                }
    }

    fun removeHashField(key: String, field: String): Mono<Void> {
        val hashKey = "${REDIS_PREFIX}::${TYPE_HASH}::${key}"

        return redisTemplate.opsForHash<String, String>().remove(hashKey, field)
                .doOnSuccess { logger.info("delete hash $key $field") }
                .doOnError { logger.error("failed to delete hash $key $field", it) }
                .then()
    }

    fun removeAll(): Mono<Void> {
        return redisOperations.keys("${REDIS_PREFIX}*")
                .flatMap { key ->
                    redisOperations.delete(key)
                            .doOnSuccess { logger.info("delete $key") }
                            .doOnError { logger.error("failed to delete $key") }
                }
                .then()
    }

    fun setJson(value: HelloDto): Mono<String> {
        val key = UUID.randomUUID().toString()
        val keyPrefixed = "${REDIS_PREFIX}::${TYPE_JSONSTR}::${key}"

        return redisTemplate.opsForValue().set(keyPrefixed, JsonUtil.convertToJsonStr(value))
                .doOnSuccess { logger.info("set $keyPrefixed $value") }
                .doOnError { logger.error("failed to set $keyPrefixed $value", it) }
                .thenReturn(key)
    }

    fun getJson(key: String): Mono<HelloDto> {
        val keyPrefixed = "${REDIS_PREFIX}::${TYPE_JSONSTR}::${key}"

        return redisTemplate.opsForValue().get(keyPrefixed)
                .switchIfEmpty {
                    Mono.error(CustomException(CustomErrorCode.NOT_FOUND))
                }
                .map { value ->
                    JsonUtil.convertToObject(value as String, HelloDto::class.java)
                }
    }
}