package me.exam.ktwebfx.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JsonUtilTest {
    @Test
    fun `TEST convertToObject`() {
        val jsonStr = "{\"id\": \"aann2\",\"name\": \"Anna\", \"age\": 15}"
        val member = JsonUtil.convertToObject(jsonStr, Member::class.java)

        assertEquals(member.id, "aann2")
        assertEquals(member.name, "Anna")
        assertEquals(member.age, 15)
    }

    data class Member(
            val id: String,
            val name: String,
            val age: Int
    )

    @Test
    fun `TEST convertToJsonStr`() {
        val member = Member("abc", "Blade", 17)
        val jsonStr = JsonUtil.convertToJsonStr(member)

        println(jsonStr)

        assertEquals(jsonStr.length, 36)
    }
}