package com.nowjordanhappy.photos_ui.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PhotoUiModule {
    @ViewModelScoped
    @Provides
    fun provideConnectivityManager(
        app: Application
    ): ConnectivityManager{
        return app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}