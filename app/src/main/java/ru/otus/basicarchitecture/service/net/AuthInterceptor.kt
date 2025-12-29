package ru.otus.basicarchitecture.service.net

import okhttp3.Interceptor
import okhttp3.Response
import ru.otus.basicarchitecture.BuildConfig

import javax.inject.Inject


class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestWithAuth = request.newBuilder()
            .header("Authorization", "Token ${BuildConfig.DADATA_API_KEY}")
            .header("X-Secret", BuildConfig.DADATA_SECRET_KEY)
            .build()

        return chain.proceed(requestWithAuth)
    }
}