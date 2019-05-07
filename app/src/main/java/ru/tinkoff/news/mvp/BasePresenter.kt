package ru.tinkoff.news.mvp

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

abstract class BasePresenter<View : MvpView> : MvpPresenter<View>() {

    private val compositeDisposable = CompositeDisposable()

    protected fun Disposable.keep() {
        compositeDisposable += this
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
