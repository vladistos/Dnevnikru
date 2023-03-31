package ru.vladik.opendiary.adapters.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vladik.opendiary.AppConstants
import ru.vladik.opendiary.R
import ru.vladik.opendiary.adapters.recyclerview.ScheduleAdapter.ViewHolder
import ru.vladik.opendiary.databinding.ScheduleItemBinding
import ru.vladik.opendiary.dnevnikapi.models.extended.ExtendedLesson
import ru.vladik.opendiary.dnevnikapi.models.v2.MarkV2
import ru.vladik.opendiary.dnevnikapi.models.v7.DaybookLessonNoPayment
import ru.vladik.opendiary.ext.isNotNull
import ru.vladik.opendiary.ext.isNull
import ru.vladik.opendiary.ext.nullIfEmpty

/**

# Адаптер для отображения расписания занятий.

Наследуется от [RecyclerDifferViewListAdapter] и реализует его абстрактные методы.

Внутренний класс [ViewHolder] содержит View-элементы для отображения данных.

Адаптер содержит приватные методы [getLessonHoursString], [getMarksString], [getMarksColor]
для обработки значений [DaybookLessonNoPayment] и списка [List] из [MarkV2].
 */
class ScheduleAdapter(val data: MutableList<ExtendedLesson>? = null) :
    RecyclerDifferViewListAdapter<ScheduleAdapter.ViewHolder, ExtendedLesson>(data) {

    inner class ViewHolder(val binding: ScheduleItemBinding): RecyclerView.ViewHolder(binding.root)

    /**

    Создает и возвращает новый [ViewHolder] для элемента списка.
     * @param parent Родительский ViewGroup, в котором будет создан новый элемент.
     * @param viewType Тип представления элемента списка.
     * @return [ViewHolder] для элемента списка.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ScheduleItemBinding.inflate(getLayoutInflater(parent), parent, false)
        return ViewHolder(binding)
    }

    /**

    Получает строковое значение, содержащее время начала и окончания урока.
    * @param lesson Урок, для которого необходимо получить строку времени.
    * @return Строка времени в формате "чч:мм - чч:мм".
     */
    private fun getLessonHoursString(lesson: DaybookLessonNoPayment): String {
        val format = AppConstants.HOUR_MINUTE_FORMAT
        val s1 = lesson.startDateTime?.let { format.format(it) }
        val s2 = lesson.endDateTime?.let { format.format(it) }
        if (s1.isNullOrEmpty() || s2.isNullOrEmpty()) return ""
        return "$s1 - $s2"
    }

    /**

    Обновляет содержимое элемента списка на основе заданной позиции и элемента данных.
     * @param holder ViewHolder для элемента списка.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.binding.run {
        val lessonV7 =  dataList[position].lessonV7
        val lessonV2 = dataList[position].lessonV2
        val marks = dataList[position].marks
        val context = holder.binding.root.context

        lessonNumber.text = lessonV7.number?.toString().orEmpty()

        if ((lessonV7.isCanceled.isNotNull() && requireNotNull(lessonV7.isCanceled)) || lessonV7.subject == null) {
            lessonSubjectTitle.text = context.getString(R.string.no_lesson)
            lessonHomeTaskLayout.visibility = View.GONE
            lessonInfoLayout.visibility = View.GONE
            lessonPlace.visibility = View.GONE
            return@run
        } else {
            lessonHomeTaskLayout.visibility = View.VISIBLE
            lessonInfoLayout.visibility = View.VISIBLE
            lessonPlace.visibility = View.VISIBLE
        }

        val lessonThemeString = lessonV7.theme.nullIfEmpty() ?: context.getString(R.string.no_theme)
        val lessonMarksString = getMarksString(marks)
        val lessonHomeTaskString = lessonV2?.homework

        if (lessonMarksString.isNull()) {
            lessonMarksContainer.visibility = View.GONE
        } else {
            lessonMarksContainer.visibility = View.VISIBLE
            lessonMarks.text = lessonMarksString
        }

        if (lessonHomeTaskString.isNull()) {
            lessonHomeTaskLayout.visibility = View.GONE
        } else {
            lessonHomeTaskLayout.visibility = View.VISIBLE
            lessonHomeTask.text = lessonHomeTaskString
        }

        getMarksColor(marks?.toList())?.let { lessonMarks.setTextColor(context.getColor(it)) }
        lessonTheme.text = lessonThemeString
        lessonPlace.text = lessonV7.place.orEmpty()
        lessonTime.text = getLessonHoursString(lessonV7)
        lessonSubjectTitle.text = lessonV7.subject.name.toString()
        lessonTeacherName.text = lessonV7.teacher?.run {
            "${lastName} ${initials}"
        }
        return@run
    }

    private fun getMarksString(marks: Iterable<MarkV2>?) = marks?.joinToString("/", transform = {it.textValue.toString()})


    /**

    Получает цвет оценки из переданной коллекции оценок [MarkV2].
    * @param marks Коллекция оценок [MarkV2], для которой необходимо получить цвет.
    * @return Ссылку [ColorRes] на цвет, или null, если переданная коллекция пуста.
     */
    private fun getMarksColor(marks: List<MarkV2>?) = if (!marks.isNullOrEmpty()) marks[0].mood?.color else null

}