package ru.otus.basicarchitecture.service

import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.otus.basicarchitecture.model.Interest
import ru.otus.basicarchitecture.model.interests


@ViewModelScoped
interface InterestsRepository {
    fun getInterests(): Flow<Set<Interest>>

    class Impl @Inject constructor() : InterestsRepository {
        override fun getInterests(): Flow<Set<Interest>> {
            return flow {
                delay(NETWORK_DELAY)
                emit(interests)
            }
        }
        companion object {
            const val NETWORK_DELAY = 1000L
        }

    }
}