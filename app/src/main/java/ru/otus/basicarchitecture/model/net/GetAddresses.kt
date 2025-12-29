package ru.otus.basicarchitecture.model.net


import ru.otus.basicarchitecture.service.net.DaDataApi
import java.io.IOException
import javax.inject.Inject

interface GetAddresses {
    suspend operator fun invoke(rawAddress : String): Suggestions

    class Impl @Inject constructor(private val api: DaDataApi) : GetAddresses {
        override suspend fun invoke(rawAddress : String): Suggestions {
            val response = api.getAddress(AddressRequest(rawAddress, 30))
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }
            return response.body() ?: throw IOException("Empty body $response")
        }
    }
}