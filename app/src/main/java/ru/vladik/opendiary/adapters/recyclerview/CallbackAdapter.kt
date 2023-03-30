package ru.vladik.opendiary.adapters.recyclerview

import androidx.recyclerview.widget.RecyclerView

/**
 * # Интерфейс для реализации в [RecyclerView].
 * ### Предоставляет переменную [callback], которая должна вызываться при нажатии на элемент.
 *
 * @property callback переменная, которая должна вызываться при нажатии на элемент.
*/

interface CallbackAdapter<T> {
    var callback: (T, Int) -> Unit
}