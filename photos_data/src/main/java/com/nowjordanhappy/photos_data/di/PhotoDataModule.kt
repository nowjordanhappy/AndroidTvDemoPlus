package com.nowjordanhappy.photos_data.di

import android.app.Application
import androidx.room.Room
import com.nowjordanhappy.photos_data.local.AppDatabase
import com.nowjordanhappy.photos_data.mapper.PhotoEntityMapper
import com.nowjordanhappy.photos_data.remote.PhotoApi
import com.nowjordanhappy.photos_data.remote.dto.mapper.PhotoDtoMapper
import com.nowjordanhappy.photos_data.repository.PhotoRepositoryImpl
import com.nowjordanhappy.photos_domain.repository.PhotoRepository
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
object PhotoDataModule {

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
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePhotoRepository(
        api: PhotoApi,
        db: AppDatabase,
    ): PhotoRepository{
        return PhotoRepositoryImpl(
            photoDao = db.photoDao,
            service = api,
            entityMapper = PhotoEntityMapper(),
            dtoMapper = PhotoDtoMapper()
        )
    }
}