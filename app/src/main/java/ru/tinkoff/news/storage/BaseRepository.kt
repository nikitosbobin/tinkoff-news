package ru.tinkoff.news.storage

import io.reactivex.Single
import org.joda.time.Duration
import ru.tinkoff.news.model.CacheMarker
import ru.tinkoff.news.orm.NewsDatabase
import kotlin.reflect.KType
import kotlin.reflect.full.functions

abstract class BaseRepository<Dao>(
    private val database: NewsDatabase,
    private val daoType: KType
) {
    @Suppress("UNCHECKED_CAST")
    protected val dao: Dao by lazy { getDaoOfType(daoType) as Dao }

    protected fun <DaoType> getDaoOfType(daoType: KType): DaoType {
        val daoGetter = NewsDatabase::class.functions
            .filter { it.isAbstract }
            .first { it.returnType == daoType }

        @Suppress("UNCHECKED_CAST")
        return daoGetter.call(database) as DaoType
    }

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
