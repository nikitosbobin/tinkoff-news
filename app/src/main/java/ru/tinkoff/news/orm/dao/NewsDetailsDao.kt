package ru.tinkoff.news.orm.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe
import ru.tinkoff.news.model.NewsItemDetails

@Dao
interface NewsDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(detailsList: List<NewsItemDetails>)

    @Query("SELECT * FROM newsitemdetails WHERE id = :id")
    fun getById(id: String): Maybe<NewsItemDetails>
}

fun NewsDetailsDao.insert(details: NewsItemDetails) {
    insert(listOf(details))
}
