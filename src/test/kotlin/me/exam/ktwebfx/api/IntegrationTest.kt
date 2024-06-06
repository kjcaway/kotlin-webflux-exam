package me.exam.ktwebfx.api

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer

@SpringBootTest(properties = ["spring.profiles.active:test"])
@ActiveProfiles("test")
@ContextConfiguration(initializers = [IntegrationTest.ContainerPropertyInitializer::class])
abstract class IntegrationTest {

    companion object {
        private var redis: GenericContainer<*>
        private val REDIS_IMAGE_NAME = "redis:6.2.7-alpine"
        private val PORT = 6379

        init {
            redis = GenericContainer(REDIS_IMAGE_NAME)
                .withExposedPorts(PORT)
                .withReuse(true)
        }

        @JvmStatic
        fun get(): GenericContainer<*> {
            return redis;
        }

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            if (!redis.isRunning()) {
                redis.start()
            }
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            redis.close()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", redis::getHost)
            registry.add("spring.data.redis.port", redis::getFirstMappedPort)
        }
    }

    internal class ContainerPropertyInitializer : ApplicationContextInitializer<ConfigurableApplicationContext?> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            println("testcontainer logs: ${redis.logs}")
        }
    }

}