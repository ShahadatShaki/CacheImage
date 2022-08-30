package com.shaki.image_cache

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.makeramen.roundedimageview.RoundedDrawable
import com.squareup.picasso.Callback
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ImageCache {


    companion object {

        private val TAG = "Cache-Image"


        fun setupCacheImage(context: Context, lifetimeInMillis: Int) {
            SharedPref.initSharedPref(context)
            OfflineCache.initOfflineCache(context)
            SharedPref.put(SharedPref.Key.LIFETIME, lifetimeInMillis)

            checkAllFiles(context)
        }

        private fun checkAllFiles(context: Context) {

            var images = OfflineCache.getOfflineList<ImageFileInfo>(OfflineCache.ALL_IMAGES)

            val daysOfYear = Calendar.getInstance().timeInMillis

            val lifetimeInDays = SharedPref.getInt(SharedPref.Key.LIFETIME)

            for (i in images.indices.reversed()) {
                val image = images[i]
                if (daysOfYear - image.lastOpened > lifetimeInDays) {
                    val file = getFile(context, image.fileName)
                    file!!.deleteOnExit()
                    images.removeAt(i)
                }
            }

            OfflineCache.saveOffline(OfflineCache.ALL_IMAGES, images)
        }

        fun loadAndStoreImage(picasso: RequestCreator, imageView: ImageView, url: String?) {
            loadAndStoreImage(picasso, imageView, url, true)
        }

        fun loadAndStoreImage(
            picasso: RequestCreator,
            imageView: ImageView,
            url: String?,
            storeImage: Boolean
        ) {

            if (SharedPref.getInstance() == null)
                throw IllegalArgumentException("Should use getInstance(Context) at least once before using this method.")


            if (url == null || url.isEmpty()) {
                return
            }

            GlobalScope.launch {
                val file = getFileNameFromUrl(imageView.context, url)
                if (file != null) {
                    if (file.length() != 0L) {
                        withContext(Dispatchers.Main) {
                            imageView.setImageURI(Uri.fromFile(file))
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            loadAndStoreImageFromPicasso(picasso, imageView, url, storeImage)
                        }
                    }
                }
            }

        }


        private fun loadAndStoreImageFromPicasso(
            picasso: RequestCreator,
            imageView: ImageView?,
            url: String,
            storeImage: Boolean
        ) {
            try {
                picasso.into(imageView, object : Callback {
                    override fun onSuccess() {
                        if (storeImage)
                            storeInLocalStorage(imageView!!, url)
                    }

                    override fun onError(e: Exception) {
                        Log.e(TAG, "onError: ${e.localizedMessage}")
                    }
                })
            } catch (e: Exception) {
                Log.e(TAG, "onError: ${e.localizedMessage}")
            }
        }

        private fun storeInLocalStorage(imageView: ImageView, url: String) {

            GlobalScope.launch {
                try {
                    var bitmap: Bitmap = try {
                        val bitmapDrawable = imageView.drawable as RoundedDrawable
                        bitmapDrawable.sourceBitmap
                    } catch (e: Exception) {
                        val bitmapDrawable = imageView.drawable as BitmapDrawable
                        bitmapDrawable.bitmap
                    }
                    val finalBitmap = bitmap
                    val file = getFileNameFromUrl(imageView.context, url)
                    if (!file?.exists()!!) file.createNewFile()
                    val out = FileOutputStream(file)
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
                    out.flush()
                    out.close()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "onError: ${e.localizedMessage}")

                }
            }

        }

        private fun getFileNameFromUrl(context: Context, url: String): File? {
            val part = url.split("/").toTypedArray()
            var imageName = part[part.size - 1].replace(".", "")

            updateImageTime(imageName)
            return getFile(context, imageName)
        }

        private fun updateImageTime(imageName: String) {
            var images = OfflineCache.getOfflineList<ImageFileInfo>(OfflineCache.ALL_IMAGES)


            for (i in images.indices.reversed()) {
                if (images[i].fileName == imageName) {
                    images[i].lastOpened = Calendar.getInstance().timeInMillis
                    OfflineCache.saveOffline(OfflineCache.ALL_IMAGES, images)
                    return
                }
            }

            val info = ImageFileInfo()
            info.lastOpened = Calendar.getInstance().timeInMillis
            info.fileName = imageName

            images.add(info)

            OfflineCache.saveOffline(OfflineCache.ALL_IMAGES, images)
        }

        private fun getFile(context: Context, imageName: String): File? {
            var file: File? = null
            file = try {
                val root = ContextWrapper(context).cacheDir.absolutePath
                val myDir = File("$root/cache")
                myDir.mkdirs()
                File(myDir, imageName)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "onError: ${e.localizedMessage}")
                return null
            }

            return file
        }


    }


}