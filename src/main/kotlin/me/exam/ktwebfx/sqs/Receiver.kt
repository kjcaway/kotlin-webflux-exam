package me.exam.ktwebfx.sqs

import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.springframework.context.annotation.Profile
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component

@Profile("prod")
@Component
class Receiver {

    @SqsListener(value = ["sqs-test-name.fifo"])
    fun listen(payload: Any, @Headers headers: MessageHeaders, acknowledgement: Acknowledgement) {
        // TODO
        acknowledgement.acknowledge()
    }
}