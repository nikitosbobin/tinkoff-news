package ru.tinkoff.news.list

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import ru.tinkoff.news.async
import ru.tinkoff.news.model.NewsItemTitle
import ru.tinkoff.news.mvp.BasePresenter
import ru.tinkoff.news.network.NetworkStateListener
import ru.tinkoff.news.sortedByDescending
import ru.tinkoff.news.storage.NewsTitlesRepository
import javax.inject.Inject

@InjectViewState
class NewsListPresenter @Inject constructor(
    private val newsTitlesRepository: NewsTitlesRepository,
    private val networkStateListener: NetworkStateListener
) : BasePresenter<NewsListView>() {

    private var previousInternetState = true

    fun subscribeToNetworkChanges() {
        networkStateListener.observeNetworkState()
            .filter { it != previousInternetState }
            .doOnNext {
                previousInternetState = it
            }
            .subscribe { connected ->
                viewState.onInternetStateChanged(connected)
            }
            .keep()
    }

    fun loadFavouriteNews() {
        viewState.showLoading(true)
        newsTitlesRepository.observeFavouriteNews()
            .flatMapSingle {
                transformNews(it)
            }
            .async()
            .subscribe({ items ->
                viewState.showLoading(false)
                viewState.showNews(items)
            }, { error ->
                viewState.showLoading(false)
                viewState.onError(error)
            })
            .keep()
    }

    fun loadNews(force: Boolean) {
        viewState.showLoading(true)
        newsTitlesRepository.getAllNews(force)
            .flatMap {
                transformNews(it)
            }
            .async()
            .doAfterTerminate { viewState.showLoading(false) }
            .subscribe({ items ->
                viewState.showNews(items)
            }, { error ->
                viewState.onError(error)
            })
            .keep()
    }

    private fun transformNews(news: List<NewsItemTitle>): Single<List<ListItem>> {
        return news.toObservable()
            .groupBy { it.publicationDate.toLocalDate() }
            .sortedByDescending { it.key!! }
            .flatMap { group ->
                group
                    .cast(ListItem::class.java)
                    .startWith(HewsTimeDivider(group.key!!.toString(DATE_FORMAT)))
            }
            .toList()
    }

    companion object {
        private const val DATE_FORMAT = "dd MMMMM, yyyy"
    }
}
