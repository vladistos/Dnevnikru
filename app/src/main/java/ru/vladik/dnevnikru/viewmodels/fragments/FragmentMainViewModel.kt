package ru.vladik.dnevnikru.viewmodels.fragments

import androidx.lifecycle.ViewModel
import ru.vladik.dnevnikru.dnevnikapi.models.v2.RecentMarksResp
import ru.vladik.dnevnikru.dnevnikapi.models.v7.UserFeedResponse
import ru.vladik.dnevnikru.viewmodels.DiaryGetViewModel
import ru.vladik.dnevnikru.viewmodels.errorhandling.ResourceLiveData

class FragmentMainViewModel : DiaryGetViewModel() {
    /**
    LiveData для получения последних оценок.
     */
    val recentMarks = ResourceLiveData<RecentMarksResp>()

    /**
    LiveData для получения новостной ленты пользователя.
     */
    val feed = ResourceLiveData<UserFeedResponse>()
}