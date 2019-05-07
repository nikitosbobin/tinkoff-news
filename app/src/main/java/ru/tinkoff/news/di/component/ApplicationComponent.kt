package ru.tinkoff.news.di.component

import dagger.Component
import ru.tinkoff.news.main.MainActivity
import ru.tinkoff.news.details.NewsItemDetailsPresenter
import ru.tinkoff.news.di.module.ApplicationModule
import ru.tinkoff.news.list.NewsListPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)

    fun getNewsListPresenter() : NewsListPresenter

    fun getNewsItemDetailsPresenter(): NewsItemDetailsPresenter
}
