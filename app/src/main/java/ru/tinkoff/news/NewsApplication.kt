package ru.tinkoff.news

import android.app.Application
import android.os.Looper
import android.os.StrictMode
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.tinkoff.news.di.component.ApplicationComponent
import ru.tinkoff.news.di.component.DaggerApplicationComponent
import ru.tinkoff.news.di.module.ApplicationModule
import timber.log.Timber

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initDi()
        initRx()
        initTimber()
        initStrictMode()
    }

    private fun initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().build())
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
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
