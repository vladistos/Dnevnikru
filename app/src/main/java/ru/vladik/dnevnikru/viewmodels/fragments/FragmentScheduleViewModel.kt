package ru.vladik.dnevnikru.viewmodels.fragments

import androidx.lifecycle.MutableLiveData
import ru.vladik.dnevnikru.dnevnikapi.DiaryApi
import ru.vladik.dnevnikru.dnevnikapi.DnevnikApiCaller
import ru.vladik.dnevnikru.dnevnikapi.models.v2.LessonV2
import ru.vladik.dnevnikru.dnevnikapi.models.v2.MarkV2
import ru.vladik.dnevnikru.dnevnikapi.models.v7.ScheduleV7
import ru.vladik.dnevnikru.ext.toIdValueMap
import ru.vladik.dnevnikru.ext.toLessonIdValueMap
import ru.vladik.dnevnikru.util.DateHelper
import ru.vladik.dnevnikru.viewmodels.DiaryGetViewModel
import ru.vladik.dnevnikru.viewmodels.errorhandling.ResourceLiveData
import ru.vladik.dnevnikru.viewmodels.launchToLiveData
import java.util.*

class FragmentScheduleViewModel : DiaryGetViewModel() {

    private val mDateHelper = DateHelper()

    private fun getCalendar() = Calendar.getInstance(TimeZone.getDefault(), Locale.ROOT).apply {
        firstDayOfWeek = Calendar.MONDAY
    }

    var selectedDay = MutableLiveData(DateHelper.Day(getCalendar(), getCalendar()))

    private val mSelectedDate
        get() = requireNotNull(selectedDay.value).calendarDay

    private val mViewingDate = mSelectedDate.clone() as Calendar

    val week
        get() = mDateHelper.getDaysOfWeekWithTimestamp(mViewingDate, mSelectedDate)

    private val mPrevWeekStartToGetNext
        get() = week[0].previousWeekDateFormattedString

    private val mWeekStart: Date
        get() = Date(week[0].startTimestamp)

    private val mWeekEnd: Date
        get() = Date(week[6].endTimestamp)

    val schedule = ResourceLiveData<ScheduleV7>()
    val prevWeekStartToGetNext = MutableLiveData(mPrevWeekStartToGetNext)
    val lessonsV2 = ResourceLiveData<Map<Long, LessonV2>>()
    val marksV2ByLessonId = ResourceLiveData<Map<Long, ArrayList<MarkV2>>>()

    fun incrementSelectedDay(): List<DateHelper.Day> = addSelectedDay(1)
    fun decrementSelectedDay(): List<DateHelper.Day> = addSelectedDay(-1)

    fun incrementViewingWeek() = addViewingDay(7)
    fun decrementViewingWeek() = addViewingDay(-7)

    fun getSchedule(api: DiaryApi) = launchToLiveData(schedule) {
        val personId = requireNotNull(api.user.personId)
        val schoolId = requireNotNull(api.user.schoolId)
        val groupId = requireNotNull(api.user.eduGroup)
        return@launchToLiveData api.getUserDayBook(personId, schoolId, groupId,
            prevWeekStartToGetNext.value, DnevnikApiCaller.ScheduleLoadType.FUTURE)
    }

    fun getV2Lessons(api: DiaryApi) = launchToLiveData(lessonsV2) {
        val groupId = requireNotNull(api.user.eduGroup)
        val lessons = api.getLessonsForPeriod(groupId, mWeekStart, mWeekEnd)
        return@launchToLiveData lessons.toIdValueMap()
    }

    fun getV2Marks(api: DiaryApi) = launchToLiveData(marksV2ByLessonId) {
        val personId = requireNotNull(api.user.personId)
        val groupId = requireNotNull(api.user.eduGroup)
        val marks = api.getMarksForPeriod(personId, groupId, mWeekStart, mWeekEnd)
        return@launchToLiveData marks.toLessonIdValueMap()
    }

    fun setSelectedDay(day: DateHelper.Day): List<DateHelper.Day> {
        selectedDay.postValue(day.copy())
        mSelectedDate.timeInMillis = day.startTimestamp
        mViewingDate.timeInMillis = day.startTimestamp
        publishDateStartIfNeeded()
        return week
    }

    private fun addSelectedDay(amount: Int): List<DateHelper.Day> {
        mSelectedDate.add(Calendar.DAY_OF_MONTH, amount)
        mViewingDate.time = mSelectedDate.time
        publishDateStartIfNeeded()
        return week
    }

    private fun addViewingDay(amount: Int) : List<DateHelper.Day> {
        mViewingDate.add(Calendar.DAY_OF_MONTH, amount)
        return week
    }

    private fun publishDateStartIfNeeded() {
        if (mPrevWeekStartToGetNext != prevWeekStartToGetNext.value)
            prevWeekStartToGetNext.postValue(mPrevWeekStartToGetNext)
    }
}