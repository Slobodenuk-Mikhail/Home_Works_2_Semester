package ru.itis.home_works_2_semester

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.home_works_2_semester.data.mapper.CharacterModelMapper
import ru.itis.home_works_2_semester.data.repository.CharacterRepositoryImpl
import ru.itis.home_works_2_semester.data.repository.UserRepositoryImpl
import ru.itis.home_works_2_semester.domain.repository.CharacterRepository
import ru.itis.home_works_2_semester.network.ApiKeyInterception
import ru.itis.home_works_2_semester.network.RnmApi
import ru.itis.home_works_2_semester.utils.GeneralExceptionHandlerImpl
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

object ServiceLocator {

    const val RNM_API_BASE_URL = "https://rickandmortyapi.com/api/"

    private val apiKeyInterceptor = ApiKeyInterception(RNM_API_BASE_URL)

//    val loggingInterceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }

    private val okHttpClient = OkHttpClient.Builder()
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(apiKeyInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(RNM_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val rnmApi = retrofit.create(RnmApi::class.java)

    fun getRnmApi() = rnmApi

    fun getGeneralExceptionHandler() = GeneralExceptionHandlerImpl(
        userRepository = UserRepositoryImpl()
    )

    fun getCharacterRepository(): CharacterRepository {
        return CharacterRepositoryImpl(
            rnmApi = getRnmApi(),
            characterModelMapper = CharacterModelMapper()
        )
    }
}