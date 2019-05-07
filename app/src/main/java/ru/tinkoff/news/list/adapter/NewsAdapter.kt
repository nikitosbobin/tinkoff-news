package ru.tinkoff.news.list.adapter

import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import ru.tinkoff.news.list.ListItem
import ru.tinkoff.news.model.NewsItemTitle

class NewsAdapter(
    onNewsItemClickListener: (NewsItemTitle, TextView) -> Unit
) : ListDelegationAdapter<List<ListItem>>() {

    init {
        delegatesManager.addDelegate(HewsTimeDividerDelegate())
        delegatesManager.addDelegate(NewsItemTitleDelegate(onNewsItemClickListener))
    }
}
