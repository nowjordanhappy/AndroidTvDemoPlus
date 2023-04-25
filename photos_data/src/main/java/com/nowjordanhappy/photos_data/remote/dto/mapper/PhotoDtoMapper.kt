package com.nowjordanhappy.photos_data.remote.dto.mapper

import android.util.Log
import com.nowjordanhappy.core.Constants
import com.nowjordanhappy.photos_data.remote.dto.PhotoDto
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos.domain.util.DateUtils
import com.nowjordanhappy.photos.domain.util.DomainNullableMapper
import java.util.*

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
            ownername = model.ownername ?: "No ownername",
            dateSaved = getDateSaved(model.dateupload)
        )
    }

    override fun mapFromDomainModel(domainModel: Photo): PhotoDto {
        return PhotoDto(
            id = domainModel.id,
            owner = domainModel.owner,
            secret = domainModel.secret ?: "",
            server = domainModel.server ?: "",
            title = domainModel.title,
            ispublic = if(domainModel.isPublic)  1 else 0,
            isfamily = if(domainModel.isFamily)  1 else 0,
            dateupload = getDateUpload(domainModel.dateUpload),
            ownername = domainModel.ownername,
        )
    }

    private fun getImageUrl(id: String, server: String?, secret: String?): String?{
        return if (secret!= null && server != null) {
            (Constants.IMAGE_BASE_URL+"$server/${id}_${secret}.jpg")
        } else null
    }

    private fun getDateUpload(dateupload: String?): String{
        return dateupload?.let {
            DateUtils.dateToString(DateUtils.longToDate(it.toLong()*1000))
        } ?: kotlin.run { "No Date" }
    }

    private fun getDateSaved(dateupload: String?): Long{
        val time = Date().time
        Log.v("PhotoEntity", "PhotoDtoMapper time: $time - dateupload: $dateupload")
        return time
    }
}