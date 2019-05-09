package ru.tinkoff.news.orm.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import ru.tinkoff.news.model.NewsItemTitle

@Dao
interface NewsTitlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(titlesList: List<NewsItemTitle>)

    @Query("SELECT * FROM newsitemtitle")
    fun getAll(): Single<List<NewsItemTitle>>

    @Query("SELECT * FROM newsitemtitle WHERE id in (SELECT newsId FROM favouritemarker)")
    fun observeFavourites(): Flowable<List<NewsItemTitle>>

    @Query("DELETE FROM newsitemtitle")
    fun deleteAll()

    @Transaction
    fun invalidateInTransaction(titlesList: List<NewsItemTitle>) {
        deleteAll()
        insert(titlesList)
    }
}
