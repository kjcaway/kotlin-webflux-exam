package me.exam.ktwebfx.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UrlUtilTest {
    @Test
    fun `TEST getRegex and match`() {
        val urls = listOf("/api/a/b", "/api/a/c", "/api/a/123")
        val pattern = "/api/a/:id"

        urls.forEachIndexed { idx, url ->
            val regex = UrlUtil.getRegex(pattern)
            val match = UrlUtil.match(url, regex)

            println("[$idx]$url, $pattern -> $match")

            assertEquals(match, true)
        }

        val urls2 = listOf("/api/a/", "/api/a", "/api/a/b/c")
        val pattern2 = "/api/a/:id"

        urls2.forEachIndexed { idx, url ->
            val regex = UrlUtil.getRegex(pattern2)
            val match = UrlUtil.match(url, regex)

            println("[$idx]$url, $pattern2 -> $match")

            assertEquals(match, false)
        }

        val urls3 = listOf("/api/a/b", "/api/a/b/c", "/api/a/b/c/d", "/api/a/")
        val pattern3 = "/api/a/*"

        urls3.forEachIndexed { idx, url ->
            val regex = UrlUtil.getRegex(pattern3)
            val match = UrlUtil.match(url, regex)

            println("[$idx]$url, $pattern3 -> $match")

            assertEquals(match, true)
        }

        val urls4 = listOf("/api/b", "/api/a", "/api/abc", "/api/*")
        val pattern4 = "/api/a/*"

        urls4.forEachIndexed { idx, url ->
            val regex = UrlUtil.getRegex(pattern4)
            val match = UrlUtil.match(url, regex)

            println("[$idx]$url, $pattern4 -> $match")

            assertEquals(match, false)
        }

        val urls5 = listOf("/api/a/b", "/api/a/c", "/api/a/123")
        val pattern5 = "/api/a/{id}"

        urls5.forEachIndexed { idx, url ->
            val regex = UrlUtil.getRegex(pattern5)
            val match = UrlUtil.match(url, regex)

            println("[$idx]$url, $pattern5 -> $match")

            assertEquals(match, true)
        }

        val urls6 = listOf("/api/a/", "/api/a", "/api/a/b/c")
        val pattern6 = "/api/a/{id}"

        urls6.forEachIndexed { idx, url ->
            val regex = UrlUtil.getRegex(pattern6)
            val match = UrlUtil.match(url, regex)

            println("[$idx]$url, $pattern6 -> $match")

            assertEquals(match, false)
        }
    }
}