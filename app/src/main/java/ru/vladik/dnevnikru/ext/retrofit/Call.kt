package ru.vladik.dnevnikru.ext.http

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.enqueue(onResponse: (resp: Response<T>) -> Unit, onErr: (err: Throwable) -> Unit) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            onResponse(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onErr(t)
        }

    })
}

fun <T> Call<T>.enqueue(onResponse: (resp: Response<T>) -> Unit) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            onResponse(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
        }

    })
}