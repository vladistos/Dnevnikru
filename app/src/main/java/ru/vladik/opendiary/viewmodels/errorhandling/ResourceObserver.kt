package ru.vladik.opendiary.viewmodels.errorhandling

import android.content.Context
import androidx.lifecycle.Observer
import ru.vladik.opendiary.func.showErrDialog

abstract class ResourceObserver<T>(var context: Context? = null) : Observer<Resource<T>>, ResourceListener<T> {

    override fun onChanged(t: Resource<T>) {
        when (t.state) {
            Resource.State.READY ->  {
                onReady(t.value!!)
            }
            Resource.State.ERROR -> onError(t.error)
            Resource.State.LOADING -> onProgress(t.loadingProgress)
        }
    }

    override fun onError(e: Resource.ResourceError?) {
        if (context == null) return
        showErrDialog(requireNotNull(context), e?.message)
    }

    override fun onProgress(p: Float) {

    }

}

