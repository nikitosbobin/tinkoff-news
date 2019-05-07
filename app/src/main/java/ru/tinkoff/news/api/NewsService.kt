package ru.tinkoff.news.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.tinkoff.news.model.NewsItemDetails
import ru.tinkoff.news.model.NewsItemTitle
import ru.tinkoff.news.model.TinkoffApiResponse

interface NewsService {

    @GET("v1/news")
    fun getAllNews(): Single<TinkoffApiResponse<List<NewsItemTitle>>>

    @GET("v1/news_content")
    fun getNewsContent(@Query("id") id: String): Single<TinkoffApiResponse<NewsItemDetails>>
}
