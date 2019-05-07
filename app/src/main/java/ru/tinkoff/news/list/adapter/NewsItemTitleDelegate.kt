package ru.tinkoff.news.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import ru.tinkoff.news.R
import ru.tinkoff.news.list.ListItem
import ru.tinkoff.news.model.NewsItemTitle

class NewsItemTitleDelegate(
    private val onNewsItemClickListener: (NewsItemTitle, TextView) -> Unit
) : AdapterDelegate<List<ListItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_item_title, parent, false)
        val holder = ViewHolder(view)
        holder.text.setOnClickListener {
            val newsItem = it.tag as NewsItemTitle
            onNewsItemClickListener(newsItem, it as TextView)
        }
        return holder
    }

    override fun isForViewType(items: List<ListItem>, position: Int): Boolean {
        return items[position] is NewsItemTitle
    }

    override fun onBindViewHolder(
        items: List<ListItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>
    ) {
        if (holder is ViewHolder) {
            holder.text.text = (items[position] as NewsItemTitle).title
            holder.text.tag = items[position]
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.text)
    }
}
