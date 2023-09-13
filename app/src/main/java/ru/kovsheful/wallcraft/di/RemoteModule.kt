package ru.kovsheful.wallcraft.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.kovsheful.wallcraft.core.ApiKeyInterceptor
import ru.kovsheful.wallcraft.data.remote.CollectionsAPI
import ru.kovsheful.wallcraft.data.repository.CollectionsRepositoryImpl
import ru.kovsheful.wallcraft.domain.repository.CollectionsRepository

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesCollectionsAPI(retrofit: Retrofit) = retrofit.create(CollectionsAPI::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
interface RemoteMindModule {
    @Binds
    fun bindCollectionsRepositoryImpl_to_CollectionsRepository(
        impl: CollectionsRepositoryImpl
    ): CollectionsRepository
}