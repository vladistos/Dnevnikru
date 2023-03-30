package ru.vladik.dnevnikru.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.vladik.dnevnikru.adapters.recyclerview.PersonChooserAdapter
import ru.vladik.dnevnikru.dnevnikapi.models.v7.ContextPersonV7

/**

Диалог выбора персоны.
* @param context контекст, в котором будет создан диалог
* @param persons список персон для выбора
* @param onPersonPick функция обратного вызова, вызываемая при выборе персоны
 *
 * @property mPersons список персон для выбора
 * @property mListener функция обратного вызова, вызываемая при выборе персоны
 */
class PersonPickerDialog(context: Context, persons: MutableList<ContextPersonV7>,
                         onPersonPick: (ContextPersonV7) -> Unit) : Dialog(context) {
    /**
    Слушатель выбора персоны.
    */
    private val mListener = onPersonPick

    /**
    Список персон.
     */
    private val mPersons = persons

    /**
     * Инициализация [RecyclerView] с [PersonChooserAdapter] в качестве адаптера, привязкa
     * [mListener] к срабатыванию [PersonChooserAdapter.onItemClickListener]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val recycler = RecyclerView(context)
        recycler.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        recycler.adapter = PersonChooserAdapter(mPersons).apply {
            callback = {contextPerson, _ ->  mListener(contextPerson)}
        }
        recycler.layoutManager = LinearLayoutManager(context)
        setContentView(recycler)
    }
}