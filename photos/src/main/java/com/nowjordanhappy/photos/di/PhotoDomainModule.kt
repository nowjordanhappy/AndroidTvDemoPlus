package com.nowjordanhappy.photos.di

import android.app.Application
import androidx.room.Room
import com.nowjordanhappy.photos.data.local.AppDatabase
import com.nowjordanhappy.photos.data.mapper.PhotoEntityMapper
import com.nowjordanhappy.photos.data.remote.PhotoApi
import com.nowjordanhappy.photos.data.remote.dto.mapper.PhotoDtoMapper
import com.nowjordanhappy.photos.data.repository.PhotoRepositoryImpl
import com.nowjordanhappy.photos.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotoDomainModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun providePhotoApi(client: OkHttpClient): PhotoApi{
        return Retrofit.Builder()
            .baseUrl(com.nowjordanhappy.core.Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providePhotoDatabase(app: Application): AppDatabase{
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "photo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEntityMapper(): PhotoEntityMapper{
        return PhotoEntityMapper()
    }

    @Provides
    @Singleton
    fun providePhotoDtoMapper(): PhotoDtoMapper{
        return PhotoDtoMapper()
    }

    @Provides
    @Singleton
    fun providePhotoRepository(
        api: PhotoApi,
        db: AppDatabase,
        entityMapper: PhotoEntityMapper,
        dtoMapper: PhotoDtoMapper,
    ): PhotoRepository{
        return PhotoRepositoryImpl(
            photoDao = db.photoDao,
            service = api,
            entityMapper = entityMapper,
            dtoMapper = dtoMapper
        )
    }
}