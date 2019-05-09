package ru.tinkoff.news

import android.app.Application
import android.os.Looper
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.tinkoff.news.di.component.ApplicationComponent
import ru.tinkoff.news.di.component.DaggerApplicationComponent
import ru.tinkoff.news.di.module.ApplicationModule

open class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initDi()
        initRx()
    }

    private fun initDi() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    private fun initRx() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            AndroidSchedulers.from(Looper.getMainLooper(), true)
        }
    }

    companion object {
        lateinit var component: ApplicationComponent
    }
}
