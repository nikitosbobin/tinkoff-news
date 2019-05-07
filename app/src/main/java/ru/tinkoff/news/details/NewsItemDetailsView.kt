package ru.tinkoff.news.details

import com.arellomobile.mvp.MvpView
import ru.tinkoff.news.model.NewsItemDetails

interface NewsItemDetailsView : MvpView {
    fun showDetails(details: NewsItemDetails, isFavourite: Boolean)

    fun showLoading(show: Boolean)

    fun onError(error: Throwable)
}
