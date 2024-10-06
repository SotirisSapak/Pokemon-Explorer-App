package com.sotirisapak.libs.pokemonexplorer.core.models

/**
 * The result default model for any api related request response
 * @author SotirisSapak
 * @since 1.0.0
 */
sealed class ApiResult<T, String> {

    /**
     * The success signal to provide as success Response when the function can return a valid [T] type
     * of return object
     * @param data the data to attach to success signal
     * @author SotirisSapak
     * @since 1.0.0
     */
    data class Success<T, String>(var data: T): ApiResult<T, String>()

    /**
     * The failure signal to provide as failure Response when the function could not return a valid [T]
     * type. Will provide an error message instead.
     * @param error the error value to provide as failure message Response
     * @author SotirisSapak
     * @since 1.0.0
     */
    data class Failure<T, String>(var error: String): ApiResult<T, String>()

    companion object {

        /**
         * Generate a new success signal
         * @param data the data type of [T] to attach to success signal
         * @author SotirisSapak
         * @since 1.0.0
         */
        fun <T, String> onSuccess(data: T) = Success<T, String>(data)

        /**
         * Generate a new failure signal
         * @param error the error value to attach to failure signal
         * @author SotirisSapak
         * @since 1.0.0
         */
        fun <T, String> onFailure(error: String) = Failure<T, String>(error)

    }

}
