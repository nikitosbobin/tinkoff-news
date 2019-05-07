package ru.tinkoff.news.di.module

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import org.joda.time.DateTime
import ru.tinkoff.news.api.ApiServiceFactory
import ru.tinkoff.news.api.DateTimeDeserializer
import ru.tinkoff.news.api.NewsService
import ru.tinkoff.news.orm.NewsDatabase
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer())
            .create()
    }

    @Provides
    @Singleton
    fun provideNewsService(gson: Gson): NewsService {
        return ApiServiceFactory.createNewsService(gson)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): NewsDatabase {
        return Room.databaseBuilder(context, NewsDatabase::class.java, "news.db")
            .build()
    }
}
