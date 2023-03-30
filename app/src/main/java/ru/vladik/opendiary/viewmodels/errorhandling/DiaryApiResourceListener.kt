package ru.vladik.opendiary.viewmodels.errorhandling

import androidx.activity.ComponentActivity
import ru.vladik.opendiary.dnevnikapi.DiaryApi
import ru.vladik.opendiary.dnevnikapi.db.models.NotNullableApiUser
import ru.vladik.opendiary.func.showErrDialog
import ru.vladik.opendiary.util.NonNullableApiUserImpl

class DiaryApiResourceListener(private val activity: ComponentActivity,
                               private val onReady: (api: DiaryApi, user: NotNullableApiUser) -> Unit) :
    ResourceListener<DiaryApi>, ResourceObserver<DiaryApi>() {

    override fun onReady(v: DiaryApi) {
        onReady(v, NonNullableApiUserImpl(v.user))
    }

    override fun onError(e: Resource.ResourceError?) = showErrDialog(activity, e?.message)
}