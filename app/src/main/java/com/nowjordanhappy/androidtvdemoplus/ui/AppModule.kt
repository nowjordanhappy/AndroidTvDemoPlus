package com.nowjordanhappy.androidtvdemoplus.ui

import android.app.Application
import com.nowjordanhappy.androidtvdemoplus.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFlickrApiKey(
        app: Application
    ): String {
        return BuildConfig.FLICKR_API_KEY
    }
}