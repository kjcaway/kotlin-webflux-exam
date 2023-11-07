package me.exam.ktwebfx.error

import org.springframework.http.HttpStatus

enum class CustomErrorCode(
        val httpStatus: HttpStatus,
        val message: String?
) {
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "bad request"),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized"),

    // 403
    FORBIDDEN(HttpStatus.FORBIDDEN, "forbidden"),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "not found"),

    // 409
    CONFLICT(HttpStatus.CONFLICT, "conflict"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
}