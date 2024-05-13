package com.ing.ingterior.injection.impl

import com.google.gson.GsonBuilder
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.db.constructor.Construction
import com.ing.ingterior.db.constructor.ConstructionRequest
import com.ing.ingterior.injection.ServerApi
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServerApiImpl : ServerApi {

    companion object{
        private const val TAG = "ServerApi"
        private const val SERVER_URL = "https://ingterior-api.com/"

        @Volatile
        private var serverApi: ServerApi? = null
        fun getApi(): ServerApi = serverApi!!

        private fun createApi() = provideRetrofit(provideOkHttpClient(), SERVER_URL).create(ServerApi::class.java)
        private fun provideOkHttpClient() = OkHttpClient
            .Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        private fun provideRetrofit(
            okHttpClient: OkHttpClient,
            BASE_URL: String
        ): Retrofit =
            Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()

    }

    init {
        if(serverApi ==null) {
            serverApi = createApi()
            logD(TAG, "init: instance is null")
        }
    }
    override fun getConstructions(memberId: Int): Observable<List<Construction>> {
        logD(TAG, "getConstructions: memberId=$memberId")
        return getApi().getConstructions(memberId)
    }

    override fun insertConstruction(request: ConstructionRequest): Observable<Int> {
        logD(TAG, "insertConstruction: request=$request")
        return getApi().insertConstruction(request)
    }

    override fun likeConstruction(memberId: Int, constructionId: Int): Observable<Int> {
        logD(TAG, "likeConstruction: memberId=$memberId, constructionId=$constructionId")
        return getApi().likeConstruction(memberId, constructionId)
    }

}