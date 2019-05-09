package ru.tinkoff.news.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.reactivex.Observable
import javax.inject.Inject

class NetworkStateListener @Inject constructor(
    private val context: Context,
    private val connectivityManager: ConnectivityManager
) {
    private val stateObservable = Observable.create<Boolean> { emitter ->
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        val networkStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                emitter.onNext(isConnected())
            }
        }
        context.registerReceiver(networkStateReceiver, intentFilter)
        emitter.setCancellable { context.unregisterReceiver(networkStateReceiver) }
    }
        .startWith(isConnected())
        .distinctUntilChanged()
        .share()

    fun observeNetworkState(): Observable<Boolean> = stateObservable

    fun isConnected(): Boolean = connectivityManager.activeNetworkInfo?.isConnected == true
}
