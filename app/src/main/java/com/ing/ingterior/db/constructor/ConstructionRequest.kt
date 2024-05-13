package com.ing.ingterior.db.constructor

data class ConstructionRequest(
    val memberId: Int,
    val usage: Int,
    val constructionName: String
)