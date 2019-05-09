package ru.tinkoff.news.storage

import io.reactivex.Single
import org.joda.time.Duration
import ru.tinkoff.news.model.CacheMarker
import ru.tinkoff.news.orm.NewsDatabase

abstract class BaseRepository<Dao>(private val database: NewsDatabase) {
    protected val dao: Dao by lazy { getDatabaseAccessObject(database) }

    protected abstract fun getDatabaseAccessObject(database: NewsDatabase): Dao

    protected fun updateCacheTimestamp(keys: List<String>) {
        val markers = keys
            .map { key ->
                CacheMarker(key, System.currentTimeMillis())
            }
            .toTypedArray()

        database.cacheMarkerDao().insertAll(*markers)
    }

    protected fun updateCacheTimestamp(key: String = defaultCacheKey) {
        database.cacheMarkerDao().insertAll(CacheMarker(key, System.currentTimeMillis()))
    }

    protected fun isCacheActualAsync(key: String = defaultCacheKey): Single<Boolean> {
        return Single.fromCallable { isCacheActual(key) }
    }

    protected fun isCacheActual(key: String = defaultCacheKey): Boolean {
        val marker = database.cacheMarkerDao().get(key) ?: return false
        val now = System.currentTimeMillis()
        return now - marker.timestamp < cacheDuration.millis
    }

    abstract val cacheDuration: Duration

    abstract val defaultCacheKey: String
}
