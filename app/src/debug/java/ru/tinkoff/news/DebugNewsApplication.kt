package ru.tinkoff.news

import android.os.StrictMode

class DebugNewsApplication : NewsApplication() {

    override fun onCreate() {
        super.onCreate()

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().build())
    }
}
