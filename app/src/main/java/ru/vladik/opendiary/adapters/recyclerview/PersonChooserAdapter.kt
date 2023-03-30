package ru.vladik.opendiary.adapters.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.vladik.opendiary.adapters.recyclerview.MarksAdapter.ViewHolder
import ru.vladik.opendiary.databinding.VhPersonCompactBinding
import ru.vladik.opendiary.dnevnikapi.models.v7.ContextPersonV7

/**
# Адаптер для отображения списка объектов [ContextPersonV7] в RecyclerView.
* Расширяет класс [RecyclerDifferViewListAdapter] и реализует интерфейс [CallbackAdapter].
* @property persons список объектов [ContextPersonV7] для отображения в адаптере.
 */
class PersonChooserAdapter(persons: MutableList<ContextPersonV7>) :
    RecyclerDifferViewListAdapter<PersonChooserAdapter.PersonVH, ContextPersonV7>(persons),
    CallbackAdapter<ContextPersonV7>{

    /**

    Функция-обработчик клика на элемент RecyclerView.
    Принимает на вход объект типа [ContextPersonV7] и индекс кликнутого элемента.
     */
    override var callback: (ContextPersonV7, Int) -> Unit = { _, _ ->  }

    class PersonVH(binding: VhPersonCompactBinding) : RecyclerView.ViewHolder(binding.root) {
        val root = binding.root
        val image = binding.image
        val titleText = binding.titleText
        val descriptionText = binding.descriptionText
    }


    /**

    Создает и возвращает новый [ViewHolder] для элемента списка.
     * @param parent Родительский ViewGroup, в котором будет создан новый элемент.
     * @param viewType Тип представления элемента списка.
     * @return [ViewHolder] для элемента списка.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonVH {
        val binding = VhPersonCompactBinding.inflate(getLayoutInflater(parent), parent, false)
        return PersonVH(binding)
    }

    /**

    Обновляет содержимое элемента списка на основе заданной позиции и элемента данных.
     * @param holder ViewHolder для элемента списка.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: PersonVH, position: Int) {
        val item = dataList[position]
        item.avatarUrl?.let { url ->
            Glide.with(holder.image).load(url).into(holder.image)
        }
        holder.titleText.text = "${item.firstName} ${item.lastName}"
        if (item.group != null) {
            holder.descriptionText.text = item.group.name
        }
        holder.root.setOnClickListener { callback(item, holder.adapterPosition) }
    }

}