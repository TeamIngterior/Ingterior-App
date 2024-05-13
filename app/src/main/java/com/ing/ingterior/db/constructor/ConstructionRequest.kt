package com.ing.ingterior.db.constructor

data class ConstInsertRequest(
    val memberId: Int,
    val usage: Int,
    val constructionName: String
)

data class ConstLikeRequest(
    val memberId: Int,
    val constructionId: Int
)