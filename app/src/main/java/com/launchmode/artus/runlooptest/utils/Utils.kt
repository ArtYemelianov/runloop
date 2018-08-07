package com.launchmode.artus.runlooptest.utils

import android.os.Looper

class Utils {

    companion object {
        fun fatalErrorIfNotMainThread() {
            if (Thread.currentThread().id != Looper.getMainLooper().thread.id) {
                val exc = RuntimeException("Called from not main thread")
                exc.printStackTrace()
                throw exc
            }
        }
    }
}