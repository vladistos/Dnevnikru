package ru.vladik.opendiary.viewmodels.fragments

import ru.vladik.opendiary.dnevnikapi.models.v2.RecentMarksResp
import ru.vladik.opendiary.dnevnikapi.models.v7.UserFeedResponse
import ru.vladik.opendiary.viewmodels.DiaryGetViewModel
import ru.vladik.opendiary.viewmodels.errorhandling.ResourceLiveData

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