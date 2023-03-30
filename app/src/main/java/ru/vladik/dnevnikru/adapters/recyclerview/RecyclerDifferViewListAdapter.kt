package ru.vladik.dnevnikru.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Базовый класс адаптера для RecyclerView, который использует AsyncListDiffer для сравнения списков элементов.
 * Реализация методов добавления и замены элементов списка может быть переопределена в классе-наследнике.
 *
 * @param VH тип ViewHolder, используемый в адаптере.
 * @param T тип элементов списка.
 * @param dataList список элементов, передаваемый в адаптер.
 * @property mDataList AsyncListDiffer, используемый для обновления списка элементов.
 * @property onItemClickListener функция, вызываемая при нажатии на элемент списка.
 */

abstract class RecyclerDifferViewListAdapter<VH : RecyclerView.ViewHolder, T : Any>(dataList: MutableList<T>? = null)
    : RecyclerView.Adapter<VH>() {
    private val mDataList: AsyncListDiffer<T> = if (dataList == null) AsyncListDiffer(this, DifferCallback())
    else AsyncListDiffer(this, DifferCallback()).apply { submitList(requireNotNull(dataList)) }

    val dataList
        get() = mDataList.currentList

    var onItemClickListener: ((i: Int, data: T, view: View) -> Unit)? = null

    /**
     * Получение объекта LayoutInflater из контекста переданного View.
     *
     * @param view View, для которой необходимо получить LayoutInflater.
     * @return LayoutInflater.
     */

    fun getLayoutInflater(view: View): LayoutInflater {
        val context = view.context
        return LayoutInflater.from(context)
    }

    /**
     * Возвращает количество элементов в списке.
     *
     * @return количество элементов.
     */

    override fun getItemCount(): Int {
        return mDataList.currentList.size
    }

    /**
     * Переопределяемые методы для изменения списка элементов. Они вызываются в соответствующих функциях адаптера.
     * Настройка поведения этих методов может быть изменена в классе-наследнике.
     *
     * @param data массив добавленных элементов.
     */

    open fun onReplaceData(data: Array<out T>) {}
    open fun onAddData(data: Array<out T>) {}
    open fun onInsertDataToBeginning(data: Array<out T>) {}

    /**
     * Добавляет элементы в конец списка.
     *
     * @param items добавляемые элементы.
     * @param callback функция, вызываемая после обновления списка.
     */

    fun addData(vararg items: T, callback: Runnable? = null) {
        val newList = ArrayList(mDataList.currentList).apply {  addAll(items.asList())  }
        mDataList.submitList(newList, callback)
        onAddData(items)
    }

    /**
     * Добавляет элементы в начало списка.
     *
     * @param items добавляемые элементы.
     * @param callback функция, вызываемая после обновления списка.
     */

    fun insertDataToBeginning(vararg items: T, callback: Runnable? = null) {
        val newList = ArrayList(items.asList()).apply {  addAll(mDataList.currentList)  }
        mDataList.submitList(newList, callback)
        onInsertDataToBeginning(items)
    }

    /**
    * Заменяет элементы на [items].
    *
    * @param items добавляемые элементы.
    * @param callback функция, вызываемая после обновления списка.
    */

    fun replaceData(vararg items: T, callback: Runnable? = null) {
        mDataList.submitList(items.asList(), callback)
        onReplaceData(items)
    }

    /**

    Проверяет, являются ли [oldItem] и [newItem] одним и тем же элементом.
    * @param oldItem старый элемент списка данных.
    * @param newItem новый элемент списка данных.
    * @return [true], если [oldItem] и [newItem] являются одним и тем же элементом, [false] в противном случае.
     */

    open fun areItemsTheSame(oldItem: T, newItem: T) : Boolean = oldItem == newItem

    /**

    Проверяет, имеют ли [oldItem] и [newItem] одинаковое содержание.
    * @param oldItem старый элемент списка данных.
    * @param newItem новый элемент списка данных.
    * @return [true], если [oldItem] и [newItem] имеют одинаковый контент, [false] в противном случае.
     */
    open fun areContentsTheSame(oldItem: T, newItem: T) : Boolean = areItemsTheSame(oldItem, newItem)

    /**

    Реализация [DiffUtil.ItemCallback] для [RecyclerDifferViewListAdapter].

    Определяет, совпадают ли элементы и содержимое элементов в старом и новом списке.
     */

    private inner class DifferCallback : DiffUtil.ItemCallback<T>() {

        /**

        Определяет, являются ли два элемента в старом и новом списке одним и тем же элементом.
        Вызывает [RecyclerDifferViewListAdapter.areItemsTheSame] для проверки равенства элементов.
        *@param oldItem элемент в старом списке
        *@param newItem элемент в новом списке
        *@return true, если элементы равны, иначе - false.
         */

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            this@RecyclerDifferViewListAdapter.areItemsTheSame(oldItem, newItem)

        /**

        Определяет, содержат ли два элемента в старом и новом списке одинаковые данные.
        Вызывает [RecyclerDifferViewListAdapter.areContentsTheSame] для проверки равенства данных элементов.
        * @param oldItem элемент в старом списке
        * @param newItem элемент в новом списке
        * @return true, если данные элементов равны, иначе - false.
         */

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            this@RecyclerDifferViewListAdapter.areContentsTheSame(oldItem, newItem)

    }
}