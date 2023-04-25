package com.nowjordanhappy.photos_data.mapper

import android.util.Log
import com.nowjordanhappy.photos_data.local.entity.PhotoEntity
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos.domain.util.DateUtils
import com.nowjordanhappy.photos.domain.util.DomainMapper
import java.util.*

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
            ownername = model.ownername,
            dateSaved = model.dateSaved
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
            ownername = domainModel.ownername,
            dateSaved = domainModel.dateSaved
            //dateSaved = getDateSaved()
        )
    }

    private fun getDateSaved(): Long{
        val time = Date().time
        Log.v("PhotoEntity", "time: $time")
        return time
    }

    /*private fun getImageUrl(id: String, server: String?, secret: String?): String?{
        return if (secret!= null && server != null) {
            (Constants.IMAGE_BASE_URL+"$server/${id}_${secret}.jpg")
        } else null
    }*/
}