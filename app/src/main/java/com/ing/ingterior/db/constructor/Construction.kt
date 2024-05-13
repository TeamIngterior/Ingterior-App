package com.ing.ingterior.db.constructor

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Construction(
    @SerializedName("constructionId") val constructionId: Int,
    @SerializedName("drawingImageUrl") val drawingImageUrl: String,
    @SerializedName("usage") val usage: Int,
    @SerializedName("constructionName") val constructionName: String,
    @SerializedName("constructionCode") val constructionCode: String,
    @SerializedName("memberCode") val memberCode: String,
    @SerializedName("regDate") val regDate: String,
    @SerializedName("updtDate") val updtDate: String,
    @SerializedName("memberImgUrls") val memberImgUrls: List<String>,
    @SerializedName("creator") val creator: Boolean,
    @SerializedName("liked") var liked: Boolean
) {

    fun convertDateTimeToLong(): Long {
        val instant = Instant.parse(regDate) // ISO 8601 형식 파싱
        return instant.toEpochMilli() // Epoch 밀리초로 변환
    }
}