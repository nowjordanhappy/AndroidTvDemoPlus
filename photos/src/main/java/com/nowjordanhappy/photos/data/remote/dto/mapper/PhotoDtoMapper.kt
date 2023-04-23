package com.nowjordanhappy.photos.data.remote.dto.mapper

import com.nowjordanhappy.core.Constants
import com.nowjordanhappy.photos.data.remote.dto.PhotoDto
import com.nowjordanhappy.photos.domain.model.Photo
import com.nowjordanhappy.photos.domain.util.DateUtils
import com.nowjordanhappy.photos.domain.util.DomainNullableMapper

class PhotoDtoMapper: DomainNullableMapper<PhotoDto, Photo> {

    override fun mapToDomainModel(model: PhotoDto): Photo? {
        return Photo(
            id = model.id ?: return null,
            owner = model.owner ?: "No owner",
            secret = model.secret ?: "",
            server = model.server ?: "",
            imageUrl = getImageUrl(model.id, model.server, model.secret),
            title = model.title ?: "No title",
            isPublic = model.ispublic == 1L,
            isFamily = model.isfamily == 1L,
            dateUpload = getDateUpload(model.dateupload),
            ownername = model.ownername ?: "No ownername"
        )
    }

    override fun mapFromDomainModel(domainModel: Photo): PhotoDto {
        TODO("Not yet implemented")
    }

    private fun getImageUrl(id: String, server: String?, secret: String?): String?{
        return if (secret!= null && server != null) {
            (Constants.IMAGE_BASE_URL+"$server/${id}_${secret}.jpg")
        } else null
    }

    private fun getDateUpload(dateupload: String?): String{
        return dateupload?.let {
            DateUtils.dateToString(DateUtils.stringToDate(it))
        } ?: kotlin.run { "No Date" }
    }
}