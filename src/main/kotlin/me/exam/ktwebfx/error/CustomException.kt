package me.exam.ktwebfx.error

class CustomException : RuntimeException {
    private lateinit var errorCode: CustomErrorCode

    constructor() : super()
    constructor(customErrorCode: CustomErrorCode) : super(customErrorCode.message) {
        this.errorCode = customErrorCode
    }

    constructor(customErrorCode: CustomErrorCode, meesage: String) : super(meesage) {
        this.errorCode = customErrorCode
    }

    constructor(customErrorCode: CustomErrorCode, meesage: String, throwable: Throwable) : super(meesage, throwable) {
        this.errorCode = customErrorCode
    }

    fun getErrorCode(): CustomErrorCode {
        return this.errorCode
    }
}