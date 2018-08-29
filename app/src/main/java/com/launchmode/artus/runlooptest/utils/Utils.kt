package com.launchmode.artus.runlooptest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Looper

class Utils constructor(private val context: Context) {

    companion object {
        fun fatalErrorIfNotMainThread() {
            if (Thread.currentThread().id != Looper.getMainLooper().thread.id) {
                val exc = RuntimeException("Called from not main thread")
                exc.printStackTrace()
                throw exc
            }
        }
    }

    fun hasConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isAvailable && activeNetwork.isConnected
    }
}