package me.exam.ktwebfx.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Bean
    fun reactiveRedisTemplate(reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Any> {
        val serializer = Jackson2JsonRedisSerializer(Any::class.java)
        val builder = RedisSerializationContext.newSerializationContext<String, Any>(StringRedisSerializer())
        val context = builder.value(serializer).build()

        return ReactiveRedisTemplate(reactiveRedisConnectionFactory, context)
    }
}