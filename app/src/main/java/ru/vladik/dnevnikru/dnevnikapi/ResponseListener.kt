package ru.vladik.dnevnikru.dnevnikapi

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResponseListener<T>(
    private val onResp: (resp: T?) -> Unit,
    private val onErr: ((t: Throwable) -> Unit)? = null

) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResp(response.body())
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onErr?.apply { this(t) }
    }

}