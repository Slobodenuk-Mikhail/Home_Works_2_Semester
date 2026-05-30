package ru.itis.home_works_2_semester.network

import android.R
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterception(
    private val getRnmApiKey: String
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${getRnmApiKey}")
            .build()

        return chain.proceed(request)
    }
}