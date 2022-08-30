package com.shaki.cacheimage

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.shaki.image_cache.ImageCache
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val image = findViewById<ImageView>(R.id.image)
        val image2 = findViewById<ImageView>(R.id.image2)

        val images = ArrayList<String>()
        images.add("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg")
        images.add("https://thumbs.dreamstime.com/b/beautiful-rain-forest-ang-ka-nature-trail-doi-inthanon-national-park-thailand-36703721.jpg")
        images.add("https://thumbs.dreamstime.com/b/rainbow-love-heart-background-red-wood-60045149.jpg")
        images.add("https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__480.jpg")
        images.add("https://c8.alamy.com/comp/2ARHPGT/historical-dhaka-city-in-bangladesh-2ARHPGT.jpg")
        images.add("https://cdn.pixabay.com/photo/2014/02/27/16/10/flowers-276014__340.jpg")
        images.add("https://images.unsplash.com/photo-1471879832106-c7ab9e0cee23?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8Mnx8fGVufDB8fHx8&w=1000&q=80")
        images.add("https://static.vecteezy.com/packs/media/vectors/term-bg-1-666de2d9.jpg")

        var p = 0
        object : CountDownTimer(300000, 5000) {
            override fun onTick(millisUntilFinished: Long) {
                if (p < images.size) {

                    val picasso = Picasso.get()
                        .load(images[p])
                        .placeholder(R.drawable.ic_place_image)
                        .error(R.drawable.ic_place_image)

                    ImageCache.loadAndStoreImage(picasso, image, images[p])
                    loadImage(image2, images[p])
                    p++
                } else
                    p = 0
            }

            override fun onFinish() {

            }
        }.start()

    }

    private fun loadImage(image: ImageView?, s: String) {
        Picasso.get()
            .load(s)
            .placeholder(R.drawable.ic_place_image)
            .error(R.drawable.ic_place_image)
            .into(image)
    }


}