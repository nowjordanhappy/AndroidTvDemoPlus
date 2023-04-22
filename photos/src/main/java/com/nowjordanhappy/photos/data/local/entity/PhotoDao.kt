package com.nowjordanhappy.photos.data.local.entity

import androidx.room.*
import com.nowjordanhappy.core.Constants.RECIPE_PAGINATION_PAGE_SIZE

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: PhotoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(recipes: List<PhotoEntity>): LongArray

    @Query("DELETE FROM photo")
    suspend fun deleteAllPhotos()

    @Query("DELETE FROM photo WHERE id = :primaryKey")
    suspend fun deletePhoto(primaryKey: Int): Int

    @Query("SELECT * FROM photo WHERE id = :id")
    suspend fun getPhotoById(id: Int): PhotoEntity?

    @Query("""
        SELECT * FROM photo 
        ORDER BY date_upload DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
    """)
    suspend fun getAllRecipes(
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE
    ): List<PhotoEntity>

    @Query("""
        SELECT * FROM photo 
        WHERE title LIKE '%' || :query || '%'
        OR ownername LIKE '%' || :query || '%'  
        ORDER BY date_upload DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
        """)
    suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE
    ): List<PhotoEntity>
}