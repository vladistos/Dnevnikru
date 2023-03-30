package ru.vladik.dnevnikru.viewmodels.errorhandling

interface ResourceListener<T> {
    fun onReady(v: T)
    fun onError(e: Resource.ResourceError?)
    fun onProgress(p: Float)
}