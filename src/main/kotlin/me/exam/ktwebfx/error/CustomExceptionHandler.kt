package me.exam.ktwebfx.error

import me.exam.ktwebfx.base.ApiResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ResponseStatusException

@Component
@Order(-2)
class CustomExceptionHandler(
    errorAttributes: ErrorAttributes,
    webProperties: WebProperties,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, webProperties.resources, applicationContext) {
    private val logger: Logger = LoggerFactory.getLogger(CustomExceptionHandler::class.java)

    init {
        super.setMessageWriters(serverCodecConfigurer.writers)
        super.setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse?>? {
        return RouterFunctions.route(RequestPredicates.all(), getBindingErrorResult)
    }

    val getBindingErrorResult = HandlerFunction { request ->
        when (val ex = super.getError(request)) {
            // CustomException 처리
            is CustomException -> {
                logger.error(ex.message, ex)

                ServerResponse.status(ex.getErrorCode().httpStatus).bodyValue(
                    ApiResult.error(ex.message)
                )
            }

            // Spring HttpException
            is ResponseStatusException -> {
                logger.error(ex.message, ex)

                ServerResponse.status(ex.statusCode).bodyValue(
                    ApiResult.error(ex.message)
                )
            }

            else -> {
                logger.error(ex.message, ex)

                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(
                    ApiResult.error(ex.message)
                )
            }
        }
    }
}