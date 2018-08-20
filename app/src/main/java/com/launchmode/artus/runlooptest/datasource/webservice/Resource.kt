package com.launchmode.artus.runlooptest.datasource.webservice

/**
 * Created by Artus
 */
class Resource<out T> constructor(val status: Status,
                                  val data: T?,
                                  val message: String?) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {

        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCESS, data, null)

        fun <T> error(data: T?, message: String?): Resource<T> = Resource(Status.ERROR, data, message)

        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data, null)
    }

    override fun toString(): String =
            status.toString() + ", data:" + data?.toString() + ", error:" + message?.toString()
}