package ru.kovsheful.wallcraft.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ImagesDao {

    @Query("SELECT * FROM images")
    suspend fun getAllImages(): List<ImageEntity>

    @Query("SELECT * FROM images WHERE localUri IS NOT NULL")
    suspend fun getDownloadedImages(): List<ImageEntity>

    @Query("SELECT * FROM images WHERE isFavorite = 1")
    suspend fun getFavoriteImages(): List<ImageEntity>

    @Upsert
    suspend fun upsertImage(image: ImageEntity)
}