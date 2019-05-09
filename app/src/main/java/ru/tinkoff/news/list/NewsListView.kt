package ru.tinkoff.news.list

import com.arellomobile.mvp.MvpView

interface NewsListView : MvpView {
    fun showNews(news: List<ListItem>)

    fun showLoading(show: Boolean)

    fun onError(error: Throwable)

    fun onInternetStateChanged(connected: Boolean)
}
