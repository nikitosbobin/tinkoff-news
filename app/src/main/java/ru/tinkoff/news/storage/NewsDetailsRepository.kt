package ru.tinkoff.news.storage

import io.reactivex.Maybe
import io.reactivex.Single
import org.joda.time.Duration
import ru.tinkoff.news.api.NewsService
import ru.tinkoff.news.model.NewsItemDetails
import ru.tinkoff.news.orm.NewsDatabase
import ru.tinkoff.news.orm.NewsDetailsDao
import ru.tinkoff.news.orm.insert
import javax.inject.Inject
import kotlin.reflect.full.createType

class NewsDetailsRepository @Inject constructor(
    database: NewsDatabase,
    private val newsService: NewsService
) : BaseRepository<NewsDetailsDao>(database, NewsDetailsDao::class.createType()) {

    fun getDetails(id: String): Single<NewsItemDetails> {
        return isCacheActualAsync()
            .filter { actual -> actual }
            .flatMap { getDetailsFromDb(id) }
            .switchIfEmpty(getDetailsFromServer(id))
            .onErrorResumeNext(getDetailsFromDb(id).toSingle())
    }

    override val cacheDuration: Duration = Duration.standardDays(1)

    override val defaultCacheKey: String = "NewsDetailsRepository"

    private fun getDetailsFromServer(id: String): Single<NewsItemDetails> {
        return newsService.getNewsContent(id)
            .map { it.payload }
            .doOnSuccess {
                dao.insert(it)
                updateCacheTimestamp()
            }
    }

    private fun getDetailsFromDb(id: String): Maybe<NewsItemDetails> {
        return dao.getById(id)
    }
}
