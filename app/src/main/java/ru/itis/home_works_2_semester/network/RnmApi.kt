package ru.itis.home_works_2_semester.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RnmApi {

    @GET("character")
    suspend fun getCharacterByName(
        @Query(value = "name") query: String
    ): RnmResponse

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id" ) id: Long): Character
}