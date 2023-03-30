package ru.vladik.opendiary.adapters.recyclerview

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import ru.vladik.opendiary.R
import ru.vladik.opendiary.databinding.DateBinding
import ru.vladik.opendiary.ext.getDp
import ru.vladik.opendiary.util.DateHelper

/**
*
* # Адаптер для отображения дней в виде списка.
* ### Расширяет класс [RecyclerDifferViewListAdapter] и работает с элементами типа [DateHelper.Day].
*
* @param data список дней, который будет отображен в адаптере. Если передается null, список будет пустым.
 */

class DayPickerAdapter(data: MutableList<DateHelper.Day>?) : RecyclerDifferViewListAdapter<DayPickerAdapter.ViewHolder, DateHelper.Day>(data) {

    private var margin: Int? = null

    /**
    ### ViewHolder для элементов списка. Содержит объект класса [DateBinding].
     */
    inner class ViewHolder(val binding: DateBinding) : RecyclerView.ViewHolder(binding.root)

    /**
    ### Позиция выбранного элемента списка. По умолчанию равна -1, то есть ни один элемент не выбран.
     */
    var selectedPos = -1

    /**
    ### Создает новый ViewHolder для элемента списка.
    * @param parent родительский ViewGroup элемента.
    * @param viewType тип View элемента.
    * @return ViewHolder для элемента списка.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DateBinding.inflate(getLayoutInflater(parent), parent, false)
        return ViewHolder(binding)
    }

    /**

    ### Вызывается, когда адаптер присоединяется к [RecyclerView]. Устанавливает отступ между элементами списка и уведомляет адаптер о необходимости обновления списка.
    * @param recyclerView RecyclerView, к которому присоединен адаптер.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.post {
            margin = ((recyclerView.width - recyclerView.context.getDp(40).toInt() * 7) / 7) - 1
            notifyDataSetChanged()
        }
    }

    /**

    ### Вызывается при замене списка элементов новым списком. Сбрасывает выбранный элемент списка.
    *@param data новый список элементов.
     */
    override fun onReplaceData(data: Array<out DateHelper.Day>) {
        selectedPos = -1
    }

    /**
    * ## Вызывается при привязке ViewHolder к элементу списка Задает параметры отображения элемента списка.
    * @param holder ViewHolder элемента списка.
    * @param position позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.binding.root.context

        margin?.let {
            val params = (holder.binding.root.layoutParams as RecyclerView.LayoutParams)
            params.marginStart = if(position == 0) it/2 else it
        }

        val day = dataList[position]

        holder.binding.apply {
            dateNumber.text = day.dateOfMonth.toString()
            if (day.isSelected) {
                root.background = AppCompatResources.getDrawable(context, R.drawable.todays_date_bg)
                dateDayOfWeek.setTextColor(context.getColor(R.color.white))
                dateNumber.setTextColor(context.getColor(R.color.white))
                selectedPos = holder.adapterPosition
                root.elevation = 10F
            } else {
                root.background = null
                val color = context.getColor(android.R.color.tab_indicator_text)
                dateDayOfWeek.setTextColor(color)
                dateNumber.setTextColor(color)
                root.elevation = 0F
            }
            dateDayOfWeek.text = day.getShortName(context)

            root.setOnClickListener { onItemClickListener?.invoke(holder.adapterPosition, day, it) }
        }
    }

    /**
    ### Определяет, одинаковые ли у двух элементов данные.
    * @param oldItem элемент, представляющий старые данные для сравнения.
    * @param newItem элемент, представляющий новые данные для сравнения.
    * @return true, если данные внутри элементов совпадают, иначе false.
     */
    override fun areContentsTheSame(oldItem: DateHelper.Day, newItem: DateHelper.Day): Boolean {
        return areItemsTheSame(oldItem, newItem) && oldItem.isSelected == newItem.isSelected &&
                oldItem.isToday == newItem.isToday
    }

}