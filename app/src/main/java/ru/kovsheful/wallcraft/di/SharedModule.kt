package ru.kovsheful.wallcraft.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kovsheful.wallcraft.core.ApiKeyInterceptor
import ru.kovsheful.wallcraft.data.local.ImagesDao
import ru.kovsheful.wallcraft.data.local.WallCraftDatabase
import ru.kovsheful.wallcraft.data.remote.CollectionsAPI
import ru.kovsheful.wallcraft.data.remote.ImageAPI
import ru.kovsheful.wallcraft.data.repository.CollectionsRepositoryImpl
import ru.kovsheful.wallcraft.data.repository.ImageRepositoryImpl
import ru.kovsheful.wallcraft.data.repository.SettingsRepositoryImpl
import ru.kovsheful.wallcraft.domain.repository.CollectionsRepository
import ru.kovsheful.wallcraft.domain.repository.ImageRepository
import ru.kovsheful.wallcraft.domain.repository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

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

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): WallCraftDatabase {
        return Room.databaseBuilder(context, WallCraftDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideImagesDao(database: WallCraftDatabase): ImagesDao = database.imagesDao()
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

    @Binds
    fun bindSettingsRepositoryImpl_to_SettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}