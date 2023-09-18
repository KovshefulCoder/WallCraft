package ru.kovsheful.wallcraft.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ImagesDao {

    @Query("SELECT * from images")
    suspend fun getAllImages(): List<ImageEntity>

    @Query("SELECT * from images WHERE localUri != NULL")
    suspend fun getLocalImages(): List<ImageEntity>

    @Upsert
    suspend fun upsertImage(image: ImageEntity)
}