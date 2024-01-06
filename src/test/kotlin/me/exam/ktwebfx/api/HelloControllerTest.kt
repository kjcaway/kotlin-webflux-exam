package me.exam.ktwebfx.api

import me.exam.ktwebfx.util.JsonUtil
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@Tag("api")
@ExtendWith(MockitoExtension::class)
@ExtendWith(SpringExtension::class)
@TestMethodOrder(MethodOrderer.MethodName::class)
@AutoConfigureWebTestClient
class HelloControllerTest @Autowired constructor(
        val webTestClient: WebTestClient
) : IntegrationTest() {

    @Value("\${external.services.test.url:}")
    private val testUrl: String? = null

    @Test
    fun `TEST_00_Test @Value`() {
        Assertions.assertEquals(testUrl, "http://localhost:8080")
    }

    @Test
    fun `TEST_01_(GET) hello OK`() {
        webTestClient.get()
                .uri("/api/hello")
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun `TEST_02_(POST) redis OK`() {
        val data = """
            {
                "key": "test",
                "field":"2",
                "value":"abc"
            }
        """.trimIndent()

        webTestClient.post()
                .uri("/api/hello/redis")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(JsonUtil.convertToMap(data)))
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun `TEST_03_(GET) redis OK`() {
        webTestClient.get()
                .uri("/api/hello/redis?key=test&field=2")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data").isEqualTo("abc")
    }

    @Test
    fun `TEST_04_(GET) redis NOT_FOUND`() {
        webTestClient.get()
                .uri("/api/hello/redis?key=notfound&field=1")
                .exchange()
                .expectStatus().isNotFound
    }
}