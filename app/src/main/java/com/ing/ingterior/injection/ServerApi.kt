package com.ing.ingterior.injection

import com.ing.ingterior.db.constructor.ConstInsertRequest
import com.ing.ingterior.db.constructor.ConstLikeRequest
import com.ing.ingterior.db.constructor.Construction
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerApi {
    @GET("construction/constructions")
    fun getConstructions(@Query("memberId") memberId: Int) : Observable<List<Construction>>

    @POST("construction/")
    fun insertConstruction(@Body request: ConstInsertRequest) : Observable<Int>

    @POST("construction/like")
    fun likeConstruction(@Body request: ConstLikeRequest) : Observable<Int>
}