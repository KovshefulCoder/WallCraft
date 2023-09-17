package ru.kovsheful.wallcraft.data.settingsManager

import android.content.SharedPreferences
import javax.inject.Inject

class SettingsManager @Inject constructor(
    private val sharedPref: SharedPreferences
) {
    fun getTheme() = sharedPref.getString("theme", "dark") ?: "dark"

    fun getNumberOfCollections() = sharedPref.getInt("collections", 15)

    fun getNumberImagesInCollection() = sharedPref.getInt("images", 15)


    fun updateTheme(isDarkTheme: Boolean) {
        sharedPref.edit().putString(
            "theme",
            if (isDarkTheme) "dark" else "light"
        ).apply()
    }

    fun updateNumberOfCollections(number: Int) {
        sharedPref.edit().putInt("collections", number).apply()
    }

    fun updateNumberImagesInCollection(number: Int) {
        sharedPref.edit().putInt("images", number).apply()
    }
}