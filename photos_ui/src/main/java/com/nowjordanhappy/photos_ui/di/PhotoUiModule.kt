package com.nowjordanhappy.photos_ui.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.nowjordanhappy.photos_ui.utils.ManagerConnection
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
    ): ManagerConnection {
        return ManagerConnection(app)
    }
}