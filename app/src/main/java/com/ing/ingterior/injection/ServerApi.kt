package com.ing.ingterior.injection

import com.ing.ingterior.db.constructor.Construction
import com.ing.ingterior.db.constructor.ConstructionRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerApi {
    @GET("construction/constructions")
    abstract fun getConstructions(@Query("memberId") memberId: Int) : Observable<List<Construction>>

    @POST("construction/")
    abstract fun insertConstruction(@Body request: ConstructionRequest) : Observable<Int>

    @FormUrlEncoded
    @POST("construction/like")
    abstract fun likeConstruction(@Field("memberId") memberId: Int, @Field("constructionId") constructionId: Int) : Observable<Int>
}