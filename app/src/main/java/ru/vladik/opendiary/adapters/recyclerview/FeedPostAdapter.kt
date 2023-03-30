package ru.vladik.opendiary.adapters.recyclerview

import android.annotation.SuppressLint
import android.text.Html
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vladik.opendiary.AppConstants
import ru.vladik.opendiary.databinding.FeedPostBinding
import ru.vladik.opendiary.dnevnikapi.models.v7.FeedInfo
import ru.vladik.opendiary.dnevnikapi.models.v7.FeedPost
import ru.vladik.opendiary.ext.loadImageWithInitialsWrapper
import ru.vladik.opendiary.ext.nullIfEmpty
import ru.vladik.opendiary.func.getInitials
import java.util.*


/**
## Адаптер для списка ленты новостей, который использует класс-помощник RecyclerDifferViewListAdapter для обновления данных.

*@param data Список данных, которые необходимо отобразить.
 */
class FeedPostAdapter(data: MutableList<FeedInfo<FeedPost>>? = null) :
    RecyclerDifferViewListAdapter<FeedPostAdapter.ViewHolder, FeedInfo<FeedPost>>(data) {

    class ViewHolder(val binding: FeedPostBinding) : RecyclerView.ViewHolder(binding.root)

    /**

    Создает новый объект [ViewHolder] путем "надувания" макета из разметки.
    * @param parent Родительский ViewGroup, в котором будет создан новый элемент.
    * @param viewType Тип View, который будет создан.
    * @return [ViewHolder] с привязкой к макету элемента списка.
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FeedPostBinding.inflate(getLayoutInflater(parent), parent, false)
        return ViewHolder(binding)
    }

    /**

    Привязывает данные к [ViewHolder].
    * @param holder ViewHolder, который будет связан с новыми данными.
    * @param position Позиция элемента в списке.
    */

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.apply {
            feedAvatar
                .loadImageWithInitialsWrapper(data.content?.authorImageUrl.nullIfEmpty() ?: data.content?.topicLogoUrl,
                getInitials(data.content?.authorFirstName, data.content?.authorMiddleName))
            data.content?.apply {
                if (!authorMiddleName.isNullOrEmpty()) {
                    feedAuthor.text = getAuthorLastNameAndInitials()
                } else {
                    feedAuthor.text = "$authorFirstName $authorLastName"
                }
                text?.let { feedText.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT) }
                createdDateTime?.let {  }
            }
            data.timeStamp?.let { timestamp ->
                val date = Date(timestamp*1000)
                feedTimeInfo.text = AppConstants.APP_DATE_FORMAT_FULL.format(date)
            }
        }
    }

    /**

    Проверяет, являются ли два элемента списка одним и тем же объектом.
    * @param oldItem Старый элемент списка.
    * @param newItem Новый элемент списка.
    * @return true, если элементы имеют одинаковый идентификатор контента и временную метку, false в противном случае.
     */
    override fun areItemsTheSame(
        oldItem: FeedInfo<FeedPost>,
        newItem: FeedInfo<FeedPost>
    ): Boolean {
        return oldItem.content?.id == newItem.content?.id && oldItem.timeStamp == newItem.timeStamp
    }
}