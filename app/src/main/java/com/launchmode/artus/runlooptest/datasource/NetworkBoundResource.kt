package com.launchmode.artus.runlooptest.datasource

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.launchmode.artus.runlooptest.datasource.webservice.Resource
import com.launchmode.artus.runlooptest.utils.AppExecutors
import java.io.IOException

/**
 * Created by Artus
 * It retrieves data from sources and manages it
 */
abstract class NetworkBoundResource<T>(private val url: String,
                                       private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<T>>()


    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromDatabase()
        result.addSource(dbSource) { data ->
            // we get here actual value from database
            result.removeSource(dbSource)
            fetchFromNetwork(dbSource)
            result.setValue(Resource.success(data))
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<T>) {

        appExecutors.networkIO().execute {

            try {
                val response = makefetch()
                if (response != null) {
                    appExecutors.diskIO().execute {
                        saveNetworkResult(response!!)

                        appExecutors.mainThread().execute {
                            val newDbSource = loadFromDatabase()
                            result.addSource(newDbSource) { newData ->
                                result.removeSource(newDbSource)
                                result.setValue(Resource.success(newData))
                            }
                        }
                    }
                } else {
                    appExecutors.mainThread().execute {
                        result.addSource(dbSource) { newData ->
                            result.setValue(Resource.error(newData, "Result is empty"))
                        }
                    }
                }
            } catch (exc: IOException) {
                appExecutors.mainThread().execute {
                    result.addSource(dbSource) { newData ->
                        result.setValue(Resource.error(newData, exc.message))
                    }
                }
            }
        }
    }

    fun asLiveData(): LiveData<Resource<T>> = result

    @WorkerThread
    protected abstract fun saveNetworkResult(data: T)

    @MainThread
    protected abstract fun loadFromDatabase(): LiveData<T>

    @WorkerThread
    protected abstract fun makefetch(): T?

}