package ru.tinkoff.news

import android.text.Spanned
import androidx.core.text.HtmlCompat
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.tinkoff.news.model.NewsItemDetails

fun Completable.async(): Completable {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.async(): Flowable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.async(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.async(): Single<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

inline fun <T : Any, K : Comparable<K>> Observable<T>.sortedBy(crossinline keySelector: (T) -> K): Observable<T> {
    return sorted { left, right -> keySelector(left).compareTo(keySelector(right)) }
}

inline fun <T : Any, K : Comparable<K>> Observable<T>.sortedByDescending(crossinline keySelector: (T) -> K): Observable<T> {
    return sorted { left, right -> keySelector(right).compareTo(keySelector(left)) }
}

fun NewsItemDetails.getHttpContent(): Spanned {
    return HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
}
