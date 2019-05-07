package ru.tinkoff.news.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import ru.tinkoff.news.R
import ru.tinkoff.news.list.HewsTimeDivider
import ru.tinkoff.news.list.ListItem

class HewsTimeDividerDelegate : AdapterDelegate<List<ListItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_date_divider, parent, false)
        return ViewHolder(view as TextView)
    }

    override fun isForViewType(items: List<ListItem>, position: Int): Boolean = items[position] is HewsTimeDivider

    override fun onBindViewHolder(
        items: List<ListItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>
    ) {
        if (holder is ViewHolder) {
            holder.text.text = (items[position] as HewsTimeDivider).text
        }
    }

    class ViewHolder(val text: TextView) : RecyclerView.ViewHolder(text)
}
