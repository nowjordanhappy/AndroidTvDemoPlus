package com.nowjordanhappy.photos_data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nowjordanhappy.photos_data.local.entity.PhotoDao
import com.nowjordanhappy.photos_data.local.entity.PhotoEntity

@Database(entities = [PhotoEntity::class ], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract val photoDao: PhotoDao

    companion object{
        val DATABASE_NAME: String = "photo_db"
    }
}