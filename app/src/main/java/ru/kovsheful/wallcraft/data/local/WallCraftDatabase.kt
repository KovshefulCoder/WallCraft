package ru.kovsheful.wallcraft.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImageEntity::class], version = 1)
abstract class WallCraftDatabase : RoomDatabase() {
    abstract fun imagesDao(): ImagesDao
}