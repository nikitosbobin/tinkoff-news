package ru.tinkoff.news.orm.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ru.tinkoff.news.model.FavouriteMarker

@Dao
interface FavouriteMarkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(marker: FavouriteMarker)

    @Query("DELETE FROM favouritemarker WHERE newsId = :id")
    fun remove(id: String)

    @Query("SELECT * FROM favouritemarker")
    fun getAll(): Single<List<FavouriteMarker>>

    @Query("SELECT count(*) FROM favouritemarker WHERE newsId = :id")
    fun contains(id: String): Single<Boolean>
}
