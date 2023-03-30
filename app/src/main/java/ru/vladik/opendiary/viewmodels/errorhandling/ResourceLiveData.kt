package ru.vladik.opendiary.viewmodels.errorhandling

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

class ResourceLiveData<T> : MutableLiveData<Resource<T>>(Resource()) {
    override fun postValue(value: Resource<T>) {
        super.postValue(value)
    }

    override fun getValue(): Resource<T> {
        return super.getValue()!!
    }

    override fun setValue(value: Resource<T>) {
        super.setValue(value)
    }

    fun postResError(message: String, cause: Throwable? = null) {
        value.postError(message, cause)
        postValue(value)
    }

    fun postResProgress(resProgress: Float) {
        value.postLoading(resProgress)
        postValue(value)
    }

    fun postResValue(resValue: T) {
        value.postValue(resValue)
        postValue(value)
    }

    fun observeValue(owner: LifecycleOwner, observer: ResourceObserver<T>) {
        if (owner is Context) {
            observer.context = owner
        }
        this.observe(owner, observer)
    }

}

