package com.shaki.cacheimage

import android.app.Application
import android.content.Context
import com.shaki.image_cache.ImageCache

/**
 * Created by SHAKI on 23-Aug-18.
 */
class MyApplication : Application() {
    private lateinit var context: Context
    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        ImageCache.setupCacheImage(context, 1000 * 60 * 60* 24 * 7) //1000 * 60 * 60* 24 * 7 = 7 Days, If any image dose not get loaded within 7 days, That image will removed from cache.

    }

}