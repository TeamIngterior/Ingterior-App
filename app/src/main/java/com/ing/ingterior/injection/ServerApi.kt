package com.ing.ingterior.injection

import com.ing.ingterior.model.SiteModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PUT

interface ServerApi {
    companion object{
        const val SERVER_URL = "http://3.39.231.71/"
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        const val s = "construction"
    }

    @PUT("construction")
    fun updateConstruction(@Body siteModel: SiteModel): Call<SiteModel>
}