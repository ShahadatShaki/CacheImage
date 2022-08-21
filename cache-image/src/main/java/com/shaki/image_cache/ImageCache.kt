package com.shaki.image_cache

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.ImageView
import com.makeramen.roundedimageview.RoundedDrawable
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ImageCache {


    companion object {

        fun loadAndStoreImage(imageView: ImageView, url: String?) {
            loadAndStoreImage(imageView, url, true)
        }

        fun loadAndStoreImage(imageView: ImageView, url: String?, storeImage: Boolean) {

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
                            loadAndStoreImageFromPicasso(imageView, url, storeImage)
                        }
                    }
                }
            }

        }

        private fun loadAndStoreImageFromPicasso(imageView: ImageView?, url: String, storeImage: Boolean) {
            try {
                Picasso.get()
                    .load(url)
//                    .placeholder(R.drawable.generic_placeholder)
//                    .error(R.drawable.generic_placeholder)
                    .into(imageView, object : Callback {
                        override fun onSuccess() {
                            if (storeImage)
                                storeInLocalStorage(imageView!!, url)
                        }

                        override fun onError(e: Exception) {
                        }
                    })
            } catch (e: Exception) {
            }
        }

        fun storeInLocalStorage(imageView: ImageView, url: String) {

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
                }
            }

        }

        private fun getFileNameFromUrl(context: Context, url: String): File? {
            var file: File? = null
            file = try {
//                val root = ContextWrapper(context).getDir("Image", Context.MODE_PRIVATE).absolutePath
                val root = ContextWrapper(context).cacheDir.absolutePath
                val myDir = File("$root/cache")
                myDir.mkdirs()
                val part = url.split("/").toTypedArray()

                var imageName = part[part.size - 1].replace(".", "")

                File(myDir, imageName)
            } catch (e: java.lang.Exception) {
                return null
            }
            return file
        }


    }


}