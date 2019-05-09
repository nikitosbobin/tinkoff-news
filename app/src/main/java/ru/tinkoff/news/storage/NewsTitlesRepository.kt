package ru.tinkoff.news.storage

import io.reactivex.*
import io.reactivex.rxkotlin.Singles
import org.joda.time.Duration
import ru.tinkoff.news.api.NewsService
import ru.tinkoff.news.model.FavouriteMarker
import ru.tinkoff.news.model.NewsItemTitle
import ru.tinkoff.news.orm.dao.FavouriteMarkerDao
import ru.tinkoff.news.orm.NewsDatabase
import ru.tinkoff.news.orm.dao.NewsTitlesDao
import javax.inject.Inject

class NewsTitlesRepository @Inject constructor(
    database: NewsDatabase,
    private val newsService: NewsService
) : BaseRepository<NewsTitlesDao>(database) {

    private val favouriteMarkerDao: FavouriteMarkerDao by lazy { database.favouriteMarkerDao() }

    fun isFavourite(newsId: String): Single<Boolean> {
        return favouriteMarkerDao.contains(newsId)
    }

    fun changeFavouriteState(newsId: String, favouriteState: Boolean): Completable {
        return Completable.fromCallable {
            if (favouriteState) {
                favouriteMarkerDao.insert(FavouriteMarker(newsId))
            } else {
                favouriteMarkerDao.remove(newsId)
            }
        }
    }

    fun observeFavouriteNews(): Flowable<List<NewsItemTitle>> {
        return isCacheActualAsync()
            .flatMapPublisher { actual ->
                if (actual) {
                    dao.observeFavourites()
                } else {
                    getFavouriteNewsFromServer()
                        .onErrorReturnItem(emptyList())
                        .toFlowable()
                        .filter { !it.isEmpty() }
                        .concatWith(dao.observeFavourites())
                }
            }
    }

    fun getAllNews(force: Boolean): Single<List<NewsItemTitle>> {
        if (force) {
            return getAllNewsFromServer()
        }

        return isCacheActualAsync()
            .filter { actual -> actual }
            .flatMap { getAllNewsFromDb() }
            .switchIfEmpty(getAllNewsFromServer())
            .onErrorResumeNext(getAllNewsFromDb().toSingle())
    }

    override fun getDatabaseAccessObject(database: NewsDatabase): NewsTitlesDao = database.newsTitlesDao()

    override val cacheDuration: Duration = Duration.standardDays(1)

    override val defaultCacheKey: String = "NewsTitlesRepository"

    private fun getFavouriteNewsFromServer(): Single<List<NewsItemTitle>> {
        val newsSingle = getAllNewsFromServer()
        val favouritesSingle = favouriteMarkerDao.getAll()

        return Singles.zip(newsSingle, favouritesSingle)
            .map { (news, favourites) ->
                val favSet = favourites.map { it.newsId }.toHashSet()
                news.filter { it.id in favSet }
            }
    }

    private fun getAllNewsFromServer(): Single<List<NewsItemTitle>> {
        return newsService.getAllNews()
            .map { it.payload }
            .doOnSuccess {
                dao.invalidateInTransaction(it)
                updateCacheTimestamp()
            }
    }

    private fun getAllNewsFromDb(): Maybe<List<NewsItemTitle>> {
        return dao.getAll()
            .filter { !it.isEmpty() }
    }
}
