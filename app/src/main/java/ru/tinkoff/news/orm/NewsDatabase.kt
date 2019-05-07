package ru.tinkoff.news.orm

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.tinkoff.news.model.CacheMarker
import ru.tinkoff.news.model.FavouriteMarker
import ru.tinkoff.news.model.NewsItemDetails
import ru.tinkoff.news.model.NewsItemTitle

@TypeConverters(Converters::class)
@Database(
    entities = [
        NewsItemDetails::class,
        NewsItemTitle::class,
        CacheMarker::class,
        FavouriteMarker::class
    ],
    version = 1
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDetailsDao(): NewsDetailsDao

    abstract fun newsTitlesDao(): NewsTitlesDao

    abstract fun cacheMarkerDao(): CacheMarkerDao

    abstract fun favouriteMarkerDao(): FavouriteMarkerDao
}
