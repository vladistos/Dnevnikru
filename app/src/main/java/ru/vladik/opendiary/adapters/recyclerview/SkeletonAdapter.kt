package ru.vladik.opendiary.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding



/**

* # Адаптер для показа каркаса списка с использованием [ViewBinding].

* @param T Тип [ViewBinding], привязанного к элементам списка.
* @property bindingGetter Функция, возвращающая ViewBinding для элементов списка.
* @property customOnBindViewHolder Функция, вызываемая для каждого элемента списка.
* @property length Длина списка.
 */
class SkeletonAdapter<T : ViewBinding>(val bindingGetter: (layoutInflater: LayoutInflater, parent: ViewGroup) -> T,
                                       val customOnBindViewHolder: (holder: ViewHolder<T>) -> Unit,
                      private val length: Int = DEFAULT_LENGTH) : RecyclerView.Adapter<SkeletonAdapter.ViewHolder<T>>() {

    class ViewHolder<T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)

    companion object {
        /**
         * Длина списка по умолчанию.
         */
        const val DEFAULT_LENGTH = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return ViewHolder(bindingGetter(LayoutInflater.from(parent.context), parent))
    }

    /**

    Обновляет содержимое элемента списка на основе заданной позиции и элемента данных.
     * @param holder ViewHolder для элемента списка.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) = customOnBindViewHolder(holder)

    /**

    Возвращает количество элементов списка.
     */
    override fun getItemCount(): Int = length
}


/**

* # Предоставляет функции для показа каркаса списка и возврата оригинального адаптера.

* @param T Тип [ViewBinding], привязанного к элементам списка.

* @property recyclerView [RecyclerView], для которого применяется адаптер.
* @property original Оригинальный адаптер для списка.
* @property adapter Адаптер, который предоставляет каркас списка.
*/
class CustomSkeleton<T : ViewBinding>(
    val recyclerView: RecyclerView,
    private val original: RecyclerView.Adapter<out RecyclerView.ViewHolder>?,
    private val adapter: SkeletonAdapter<T>) {

    fun showSkeleton() { recyclerView.adapter = adapter }
    fun showOriginal() { recyclerView.adapter = original }
}
/**

* ## Применяет каркас списка к [RecyclerView].

* @param T Тип [ViewBinding], привязанного к элементам списка.
* @param bindingGetter Функция, возвращающая [ViewBinding] для элементов списка.
* @param customOnBindViewHolder Функция, вызываемая для каждого элемента списка.
* @param length Длина списка.
* @return [CustomSkeleton] Объект [CustomSkeleton], предоставляющий функции для показа каркаса списка и возврата оригинального адаптера.
 */
fun <T: ViewBinding> RecyclerView.applyCustomSkeleton(bindingGetter: (layoutInflater: LayoutInflater, parent: ViewGroup) -> T,
                                                      customOnBindViewHolder: (holder: SkeletonAdapter.ViewHolder<T>) -> Unit,
                                                      length: Int = SkeletonAdapter.DEFAULT_LENGTH) =
    CustomSkeleton(this, adapter, SkeletonAdapter(bindingGetter, customOnBindViewHolder, length))