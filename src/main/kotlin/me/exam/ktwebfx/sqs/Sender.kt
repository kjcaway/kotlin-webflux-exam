package me.exam.ktwebfx.sqs

import io.awspring.cloud.sqs.operations.SendResult
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("prod")
@Component
class Sender(
        val sqsTemplate: SqsTemplate
) {

    fun send(payload: String): SendResult<String> {
        return sqsTemplate.send { sendOpsTo ->
            sendOpsTo
                    .queue("sqs-test-name.fifo")
                    .headers(mapOf(
                            "x-header" to "abc"
                    ))
                    .payload(payload)
        }
    }
}