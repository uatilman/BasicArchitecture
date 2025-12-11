package ru.otus.basicarchitecture.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ru.otus.basicarchitecture.service.DaDataService
import ru.otus.basicarchitecture.service.DaDataServiceImpl
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCase
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun daDataService(): DaDataService = DaDataServiceImpl()


    @Provides
    fun addressSuggestUseCase(): AddressSuggestUseCase = AddressSuggestUseCaseImpl()
}