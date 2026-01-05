package ru.otus.basicarchitecture.config

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.otus.basicarchitecture.BuildConfig
import ru.otus.basicarchitecture.model.net.GetAddresses
import ru.otus.basicarchitecture.service.DaDataService
import ru.otus.basicarchitecture.service.InterestsRepository
import ru.otus.basicarchitecture.service.net.AuthInterceptor
import ru.otus.basicarchitecture.service.net.DaDataApi
import ru.otus.basicarchitecture.service.net.buildRetrofit
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCase
import ru.otus.basicarchitecture.use_case.FieldValidationUseCase
import ru.otus.basicarchitecture.view_model.WizardCache
import kotlin.time.Duration


@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun wizardCache(impl: WizardCache.Impl): WizardCache
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun interestsRepository(impl: InterestsRepository.Impl): InterestsRepository

    @Binds
    @Singleton
    abstract fun daDataService(impl: DaDataService.Impl): DaDataService

    @Binds
    abstract fun fieldValidationUseCase(impl: FieldValidationUseCase.Impl): FieldValidationUseCase

    @Binds
    abstract fun addressSuggestUseCase(impl: AddressSuggestUseCase.Impl): AddressSuggestUseCase

    @Binds
    @Singleton
    abstract fun getAddressesCommand(impl: GetAddresses.Impl): GetAddresses
}

@Module
@InstallIn(SingletonComponent::class)
class NetModuleProvider {
    @Provides
    @Singleton
    fun okHttp(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(Duration.parse(BuildConfig.LOAD_ADDRESS_TIMOUT))
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        })
        .build()

    @Provides
    @Singleton
    fun retrofit(okHttp: OkHttpClient): Retrofit = buildRetrofit(okHttp)

    @Provides
    @Singleton
    fun api(retrofit: Retrofit): DaDataApi = retrofit.create(DaDataApi::class.java)
}