package ru.otus.basicarchitecture.config

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ru.otus.basicarchitecture.service.DaDataService
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCase
import ru.otus.basicarchitecture.use_case.FieldValidationUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun daDataService(impl: DaDataService.Impl): DaDataService

}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun fieldValidationUseCase(impl: FieldValidationUseCase.Impl): FieldValidationUseCase

    @Binds
    abstract fun addressSuggestUseCase(impl: AddressSuggestUseCase.Impl): AddressSuggestUseCase
}