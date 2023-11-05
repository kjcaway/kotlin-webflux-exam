package me.exam.ktwebfx.config

import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JsonPropertySourceFactory: PropertySourceFactory {
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val typeOfMap: TypeReference<Map<String, Any>> = object : TypeReference<Map<String, Any>>() {}
        val values = jacksonObjectMapper().readValue(resource.inputStream, typeOfMap)

        return MapPropertySource("json-env", values)
    }
}