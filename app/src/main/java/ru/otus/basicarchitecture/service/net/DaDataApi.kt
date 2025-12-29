package ru.otus.basicarchitecture.service.net

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import ru.otus.basicarchitecture.model.net.AddressRequest
import ru.otus.basicarchitecture.model.net.Suggestions

private const val baseUrl = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/"

interface DaDataApi {
    @POST("address")
    suspend fun getAddress(@Body addressRequest: AddressRequest): Response<Suggestions>
}

fun buildRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .build()
