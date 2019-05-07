package ru.tinkoff.news.orm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.tinkoff.news.model.CacheMarker

@Dao
interface CacheMarkerDao {

    @Query("SELECT * FROM cachemarker WHERE cacheKey=:key")
    fun get(key: String): CacheMarker?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg markers: CacheMarker)
}
