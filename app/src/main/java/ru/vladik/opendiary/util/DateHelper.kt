package ru.vladik.opendiary.util

import android.content.Context
import ru.vladik.opendiary.AppConstants
import ru.vladik.opendiary.R
import ru.vladik.opendiary.dnevnikapi.models.Entity
import ru.vladik.opendiary.ext.isNull
import java.util.*

class DateHelper {

    data class Day(
        val calendarDay: Calendar,
        val selectedDate: Calendar
    ) : Entity {

        override val id
            get() = startTimestamp

        private val pendosIndex = calendarDay.get(Calendar.DAY_OF_WEEK)
        private val selectedTimestamp = selectedDate.timeInMillis
        val dateOfMonth = calendarDay.get(Calendar.DAY_OF_MONTH)
        val startTimestamp = calendarDay.timeInMillis
        val endTimestamp = calendarDay.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        val isToday
            get() = Date().time in startTimestamp..endTimestamp

        val isSelected = selectedTimestamp in startTimestamp..endTimestamp

        val ruIndex: Int
            get() {
                val new = pendosIndex - 1
                return if (new != 0) new else 7
            }

        private val previousCalendarDay
            get() = (calendarDay.clone() as Calendar).apply { add(Calendar.WEEK_OF_YEAR, -1) }

        val previousWeekDateFormattedString: String =
            AppConstants.DIARY_WEEK_START_DATE_FORMAT.format(previousCalendarDay.time)

        fun getShortName(context: Context): String {
            return context.resources.getStringArray(R.array.days_of_week_short)[ruIndex - 1]
        }

        override fun hashCode(): Int = (startTimestamp and endTimestamp).hashCode()

        override fun equals(other: Any?): Boolean {
            return if (other.isNull() || !(other is Day)) false else other.startTimestamp == startTimestamp
        }
    }

    private fun resetForDayStart(calendar: Calendar) = calendar.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    fun getDaysOfWeekWithTimestamp(viewingDate: Calendar, selectedDate: Calendar, weekOffset: Int = 0) : MutableList<Day> {
        resetForDayStart(viewingDate)
        viewingDate.add(Calendar.WEEK_OF_YEAR, weekOffset)

        val daysOfWeek = mutableListOf<Day>()

        for (i in 1..7) {
            viewingDate.set(Calendar.DAY_OF_WEEK, i)
            daysOfWeek.add(
                Day(
                    viewingDate.clone() as Calendar, selectedDate.clone() as Calendar
                )
            )
        }

        Collections.rotate(daysOfWeek, -1)

        return daysOfWeek
    }
}