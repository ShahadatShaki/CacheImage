package com.shaki.cacheimage

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.shaki.image_cache.ImageCache
import 	java.lang.String
import kotlin.jvm.internal.Intrinsics

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val image = findViewById<ImageView>(R.id.image)

        ImageCache.loadAndStoreImage(image, "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg")


    }

}