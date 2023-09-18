package ru.kovsheful.wallcraft.data.imageManagers

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kovsheful.wallcraft.core.ErrorWhileSetWallpaper
import ru.kovsheful.wallcraft.core.SmthWentWrongWhileSetWallpaper
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class ImageWallpaperManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    private val wallpaperManager = WallpaperManager.getInstance(context)

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun setImageAsWallpaper(imageUrl: String, wallpaperType: Int = 0) {
        try {
            val request = DownloadManager.Request(imageUrl.toUri())
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "wallpaper.jpg")
            val imageID = downloadManager.enqueue(request)
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == "android.intent.action.DOWNLOAD_COMPLETE") {
                        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                        if (id == imageID) {
                            val downloadUri = downloadManager.getUriForDownloadedFile(id)
                            val inputStream = context.contentResolver.openInputStream(downloadUri)
                            when (wallpaperType) {
                                0 -> wallpaperManager.setStream(inputStream)
                                else -> {
                                    wallpaperManager.setStream(
                                        inputStream,
                                        null,
                                        true,
                                        wallpaperType
                                    )
                                }
                            }
                            val documentFile = DocumentFile.fromSingleUri(context, downloadUri);
                            if (documentFile != null && documentFile.exists()) {
                                documentFile.delete();
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
        } catch (e: IOException) {
            Log.i("ImageWallpaperManager", "setImageAsWallpaper IOException")
            e.printStackTrace()
            throw ErrorWhileSetWallpaper(message = "Couldn't install this photo as a wallpaper")
        } catch (e: FileNotFoundException) {
            Log.i("ImageWallpaperManager", "setImageAsWallpaper IOException")
            e.printStackTrace()
            throw ErrorWhileSetWallpaper(message = "Error with downloading photo")
        } catch (e: Exception) {
            Log.i("ImageWallpaperManager", "setImageAsWallpaper IOException")
            e.printStackTrace()
            throw SmthWentWrongWhileSetWallpaper(message = "Something definitely went wrong")
        }
    }
}