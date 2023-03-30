package ru.vladik.dnevnikru.adapters.recyclerview

/**
 * Интерфейс для реализации в [androidx.recyclerview.widget.RecyclerView]. Предоставляет
 * переменную [callback], которая должна вызываться при нажатии на элемент.
 *
 * @property callback переменная, которая должна вызываться при нажатии на элемент.
*/

interface CallbackAdapter<T> {
    var callback: (T, Int) -> Unit
}