package ru.kovsheful.wallcraft.data.imageManagers

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kovsheful.wallcraft.data.local.ImageEntity
import ru.kovsheful.wallcraft.data.local.ImagesDao
import javax.inject.Inject

class ImageDownloadManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imagesDao: ImagesDao
) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    suspend fun downloadImage(imageUrl: String, imageApiID: Int, visibilityStatus: Int) =
        withContext(Dispatchers.IO) {
            val request = DownloadManager.Request(imageUrl.toUri())
                .setNotificationVisibility(visibilityStatus)
                .setTitle("")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "wallpaper.jpg")
            val imageID = downloadManager.enqueue(request)
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == "android.intent.action.DOWNLOAD_COMPLETE") {
                        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                        if (id == imageID) {
                            val imageUri = downloadManager.getUriForDownloadedFile(id).toString()
                            CoroutineScope(Dispatchers.IO).launch {
                                imagesDao.upsertImage(ImageEntity(
                                    id = imageApiID,
                                    url = imageUrl,
                                    localUri = imageUri
                                ))
                            }
                        }
                    }
                    context.unregisterReceiver(this)
                }
            }
            context.registerReceiver(
                receiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
        }
}