package ru.vladik.opendiary.adapters.recyclerview

import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vladik.opendiary.databinding.MarkElementBinding
import ru.vladik.opendiary.dnevnikapi.models.v2.RecentMarksResp

/**
 * # Адаптер для отображения списка оценок на главной.
 * @constructor Возвращает новый экземпляр класса
 * @see RecyclerDifferViewListAdapter
 */

class MarksAdapter(data: MutableList<RecentMarksResp.ExtendedMark>? = null)
    : RecyclerDifferViewListAdapter<MarksAdapter.ViewHolder, RecentMarksResp.ExtendedMark>(data) {

    inner class ViewHolder(val binding: MarkElementBinding) : RecyclerView.ViewHolder(binding.root)

    /**

    Создает и возвращает новый [ViewHolder] для элемента списка.
    * @param parent Родительский ViewGroup, в котором будет создан новый элемент.
    * @param viewType Тип представления элемента списка.
    * @return [ViewHolder] для элемента списка.
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MarkElementBinding.inflate(getLayoutInflater(parent), parent, false))
    }

    /**

    Обновляет содержимое элемента списка на основе заданной позиции и элемента данных.
    * @param holder ViewHolder для элемента списка.
    * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.binding.root.context
        val mark = dataList[position]
        holder.binding.apply {
            markText.text = mark.markV2.textValue
            mark.subject?.name?.let { markSubject.text = it }
            mark.markV2.mood?.color?.let {
                (backgroundView.background as GradientDrawable).setStroke(3, context.getColor(it))
            }
        }
    }

}