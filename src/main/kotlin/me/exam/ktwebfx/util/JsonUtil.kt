package me.exam.ktwebfx.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.exam.ktwebfx.error.CustomErrorCode
import me.exam.ktwebfx.error.CustomException

class JsonUtil {
    companion object {
        private val mapper = jacksonObjectMapper()
        private val typeOfMap: TypeReference<Map<String, Any>> = object : TypeReference<Map<String, Any>>() {}

        /**
         * Get Object from json string
         */
        fun <T> convertToObject(jsonStr: String, type: Class<T>): T {
            return try {
                mapper.readValue(jsonStr, type)
            } catch (e: Exception) {
                throw CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR, "json parse error", e)
            }
        }

        /**
         * Get json string from Object
         */
        fun convertToJsonStr(obj: Any): String {
            return try {
                mapper.writeValueAsString(obj)
            } catch (e: Exception) {
                throw CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR, "json parse error", e)
            }
        }

        /**
         * Get map from Object
         *
         * @param jsonStr String?
         * @return Map
         */
        fun convertToMap(jsonStr: String): Map<String, Any> {
            return try {
                mapper.readValue(jsonStr, typeOfMap)
            } catch (e: Exception) {
                throw CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR, "json parse error", e)
            }
        }
    }
}