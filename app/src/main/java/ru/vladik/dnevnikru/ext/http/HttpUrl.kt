package ru.vladik.dnevnikru.ext.http

import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.Response
import java.io.IOException


fun HttpUrl.addParam(k: String, v: String): HttpUrl {
    return this.newBuilder().apply {
        addQueryParameter(k, v)
    }.build()
}

fun Call.enqueue(onResponse: (resp: Response) -> Unit) {
    this.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            onResponse(response)
        }

        override fun onFailure(call: Call, e: IOException) {
        }

    })
}

fun Call.enqueue(onResponse: (resp: Response) -> Unit, onError: (e: IOException) -> Unit) {
    this.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            onResponse(response)
        }

        override fun onFailure(call: Call, e: IOException) {
            onError(e)
        }

    })
}