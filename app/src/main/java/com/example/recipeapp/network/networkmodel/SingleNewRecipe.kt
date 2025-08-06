package com.example.recipeapp.network.networkmodel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleNewRecipe(
    @SerialName("id")
    val id: Int,
    @SerialName("image")
    val image: String,
    @SerialName("imageType")
    val imageType: String,
    @SerialName("title")
    val title: String,
    @SerialName("readyInMinutes")
    val readyInMinutes: Int,
    @SerialName("sourceName")
    val sourceName: String
)