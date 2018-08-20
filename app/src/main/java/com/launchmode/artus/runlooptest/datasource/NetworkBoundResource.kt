package com.launchmode.artus.runlooptest.datasource

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.artus.rssreader.Article
import com.launchmode.artus.runlooptest.datasource.webservice.Parser
import com.launchmode.artus.runlooptest.datasource.webservice.Resource
import com.launchmode.artus.runlooptest.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Created by Artus
 */
abstract class NetworkBoundResource<T>(private val url: String,
                                       private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<T>>()
    private val mClient = OkHttpClient()

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
                val response = execute()
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

    /**
     * Retrieves newest data from server
     */
    private fun execute(): T? {
        val request = Request.Builder()
                .url(url)
                .build()
        val response = mClient.newCall(request).execute()
        val body = response.body()?.string() ?: return null
        val list = Parser(body!!).parse() ?: return null
        return convert(list)
    }

    fun asLiveData(): LiveData<Resource<T>> = result

    @WorkerThread
    protected abstract fun convert(data: List<Article>): T

    @WorkerThread
    protected abstract fun saveNetworkResult(data: T)

    @MainThread
    protected abstract fun loadFromDatabase(): LiveData<T>

}