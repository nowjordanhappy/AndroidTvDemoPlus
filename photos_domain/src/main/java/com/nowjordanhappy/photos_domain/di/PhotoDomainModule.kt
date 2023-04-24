package com.nowjordanhappy.photos_domain.di

import com.nowjordanhappy.photos_domain.repository.PhotoRepository
import com.nowjordanhappy.photos_domain.use_case.GetPhotoDetail
import com.nowjordanhappy.photos_domain.use_case.GetRecentPhotos
import com.nowjordanhappy.photos_domain.use_case.PhotoUseCases
import com.nowjordanhappy.photos_domain.use_case.SearchPhotos
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PhotoDomainModule {
    @ViewModelScoped
    @Provides
    fun providePhotoUseCases(
        repository: PhotoRepository
    ): PhotoUseCases{
        return PhotoUseCases(
            searchPhotos = SearchPhotos(repository),
            getRecentPhotos = GetRecentPhotos(repository),
            getPhotoDetail = GetPhotoDetail(repository),
        )
    }
}