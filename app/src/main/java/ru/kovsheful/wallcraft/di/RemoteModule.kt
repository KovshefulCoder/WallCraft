package ru.kovsheful.wallcraft.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.kovsheful.wallcraft.core.ApiKeyInterceptor
import ru.kovsheful.wallcraft.data.remote.CollectionsAPI
import ru.kovsheful.wallcraft.data.remote.ImageAPI
import ru.kovsheful.wallcraft.data.repository.CollectionsRepositoryImpl
import ru.kovsheful.wallcraft.data.repository.ImageRepositoryImpl
import ru.kovsheful.wallcraft.domain.repository.CollectionsRepository
import ru.kovsheful.wallcraft.domain.repository.ImageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesCollectionsAPI(retrofit: Retrofit): CollectionsAPI = retrofit.create(CollectionsAPI::class.java)

    @Provides
    fun providesImageAPI(retrofit: Retrofit): ImageAPI = retrofit.create(ImageAPI::class.java)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface RemoteBindModule {
    @Binds
    fun bindCollectionsRepositoryImpl_to_CollectionsRepository(
        impl: CollectionsRepositoryImpl
    ): CollectionsRepository

    @Binds
    fun bindImageRepositoryImpl_to_ImageRepository(
        impl: ImageRepositoryImpl
    ): ImageRepository
}