package ru.vladik.dnevnikru.viewmodels.errorhandling

import androidx.activity.ComponentActivity
import ru.vladik.dnevnikru.dnevnikapi.DiaryApi
import ru.vladik.dnevnikru.dnevnikapi.db.models.NotNullableApiUser
import ru.vladik.dnevnikru.func.showErrDialog
import ru.vladik.dnevnikru.util.NonNullableApiUserImpl

class DiaryApiResourceListener(private val activity: ComponentActivity,
                               private val onReady: (api: DiaryApi, user: NotNullableApiUser) -> Unit) :
    ResourceListener<DiaryApi>, ResourceObserver<DiaryApi>() {

    override fun onReady(v: DiaryApi) {
        onReady(v, NonNullableApiUserImpl(v.user))
    }

    override fun onError(e: Resource.ResourceError?) = showErrDialog(activity, e?.message)
}