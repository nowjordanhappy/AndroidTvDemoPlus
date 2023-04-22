package com.nowjordanhappy.photos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nowjordanhappy.photos.data.local.entity.PhotoDao
import com.nowjordanhappy.photos.data.local.entity.PhotoEntity

@Database(entities = [PhotoEntity::class ], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object{
        val DATABASE_NAME: String = "photo_db"
    }
}