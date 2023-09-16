package ru.kovsheful.wallcraft.data.imageManagers

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageDownloadManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    suspend fun downloadImage(imageUrl: String, visibilityStatus: Int): Uri =
        withContext(Dispatchers.IO) {
            val request = DownloadManager.Request(imageUrl.toUri())
                .setNotificationVisibility(visibilityStatus)
                .setTitle("")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "wallpaper.jpg")
            val imageID = async {
                downloadManager.enqueue(request)
            }
            return@withContext downloadManager.getUriForDownloadedFile(imageID.await())
        }
    }