package ru.tinkoff.news.details

import com.arellomobile.mvp.InjectViewState
import io.reactivex.rxkotlin.Singles
import ru.tinkoff.news.async
import ru.tinkoff.news.mvp.BasePresenter
import ru.tinkoff.news.storage.NewsDetailsRepository
import ru.tinkoff.news.storage.NewsTitlesRepository
import javax.inject.Inject

@InjectViewState
class NewsItemDetailsPresenter @Inject constructor(
    private val newsDetailsRepository: NewsDetailsRepository,
    private val newsTitlesRepository: NewsTitlesRepository
) : BasePresenter<NewsItemDetailsView>() {

    private lateinit var newsItemId: String

    fun loadDetails(newsItemId: String) {
        this.newsItemId = newsItemId

        viewState.showLoading(true)
        Singles.zip(
            newsDetailsRepository.getDetails(newsItemId),
            newsTitlesRepository.isFavourite(newsItemId)
        )
            .async()
            .doAfterTerminate { viewState.showLoading(false) }
            .subscribe({ (details, isFavourite) ->
                viewState.showDetails(details, isFavourite)
            }, { error ->
                viewState.onError(error)
            })
            .keep()
    }

    fun changeFavouriteState(inFavourite: Boolean) {
        newsTitlesRepository.changeFavouriteState(newsItemId, inFavourite)
            .async()
            .subscribe()
            .keep()
    }
}
