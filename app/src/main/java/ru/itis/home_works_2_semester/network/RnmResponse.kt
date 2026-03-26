package ru.itis.home_works_2_semester.network

import coil3.Image
import com.google.gson.annotations.SerializedName

data class RnmResponse(
    @SerializedName(value = "results")
    val result: List<Character>?
)

data class Character(
    @SerializedName(value = "id")
    val id: Long?,
    @SerializedName(value = "name")
    val name: String?,
    @SerializedName(value = "status")
    val status: String?,
    @SerializedName(value = "species")
    val species: String?,
    @SerializedName(value = "gender")
    val gender: String?,
    @SerializedName(value = "image")
    val image: String?
)
