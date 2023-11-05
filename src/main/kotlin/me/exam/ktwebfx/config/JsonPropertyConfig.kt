package me.exam.ktwebfx.config

import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment

@Configuration
@PropertySource(
        value = ["file:\${json-env-file-path}"],
        ignoreResourceNotFound = true,
        factory = JsonPropertySourceFactory::class
)
class JsonPropertyConfig(
        val environment: Environment
) {
    private val logger: Logger = LoggerFactory.getLogger(JsonPropertyConfig::class.java)

    /* external property file. read file and set value as properties field */
    @Value("\${json-env.secret-key:}")
    var secretKey: String? = null

    @Value("\${spring.profiles.active:}")
    private val activeProfile: String? = null

    @PostConstruct
    fun init() {
        logger.info("init json property. active profile: $activeProfile")

        // if you need to set value from file(like .json)
        // reallocate field value
        secretKey = environment.getProperty("secret-key")
    }
}