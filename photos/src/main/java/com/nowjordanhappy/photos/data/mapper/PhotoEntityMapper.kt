package com.nowjordanhappy.photos.data.mapper

import com.nowjordanhappy.core.Constants
import com.nowjordanhappy.photos.data.local.entity.PhotoEntity
import com.nowjordanhappy.photos.domain.model.Photo
import com.nowjordanhappy.photos.domain.util.DateUtils
import com.nowjordanhappy.photos.domain.util.DomainMapper

class PhotoEntityMapper: DomainMapper<PhotoEntity, Photo> {
    override fun mapToDomainModel(model: PhotoEntity): Photo {
        return Photo(
            id = model.id,
            owner = model.owner,
            secret = model.secret,
            server = model.server,
            imageUrl = model.imageUrl,
            title = model.title,
            isPublic = model.isPublic,
            isFamily = model.isFamily,
            dateUpload = DateUtils.dateToString(DateUtils.longToDate(model.dateUpload)),
            ownername = model.ownername
        )
    }

    override fun mapFromDomainModel(domainModel: Photo): PhotoEntity {
        return PhotoEntity(
            id = domainModel.id,
            owner = domainModel.owner,
            secret = domainModel.secret,
            server = domainModel.server,
            imageUrl = domainModel.imageUrl,
            title = domainModel.title,
            isPublic = domainModel.isPublic,
            isFamily = domainModel.isFamily,
            dateUpload = DateUtils.dateToLong(DateUtils.stringToDate(domainModel.dateUpload)),
            ownername = domainModel.ownername
        )
    }

    /*private fun getImageUrl(id: String, server: String?, secret: String?): String?{
        return if (secret!= null && server != null) {
            (Constants.IMAGE_BASE_URL+"$server/${id}_${secret}.jpg")
        } else null
    }*/
}