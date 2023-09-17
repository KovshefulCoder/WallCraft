package ru.kovsheful.wallcraft.data.settingsManager

import android.content.SharedPreferences
import javax.inject.Inject

private const val THEME = "isDarkTheme"
private const val NUMBER_OF_COLLECTIONS = "collections"
private const val NUMBER_OF_IMAGES = "images"

class SettingsManager @Inject constructor(
    private val sharedPref: SharedPreferences
) {
    fun getTheme() = sharedPref.getBoolean(THEME, true)

    fun getNumberOfCollections() = sharedPref.getInt(NUMBER_OF_COLLECTIONS, 15)

    fun getNumberImagesInCollection() = sharedPref.getInt(NUMBER_OF_IMAGES, 15)


    fun updateTheme(isDarkTheme: Boolean) {
        sharedPref.edit().putBoolean(THEME, isDarkTheme).apply()
    }

    fun updateNumberOfCollections(number: Int) {
        sharedPref.edit().putInt(NUMBER_OF_COLLECTIONS, number).apply()
    }

    fun updateNumberImagesInCollection(number: Int) {
        sharedPref.edit().putInt(NUMBER_OF_IMAGES, number).apply()
    }
}