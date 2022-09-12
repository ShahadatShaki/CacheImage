# CacheImage
Most of us use Glide and Picasso library for loading the image from the network. Picasso and Glide both cache images upto 50MB. 


This Library store the loaded image in our local storage. Next time load the image from the local storage. You can store as much image as you like as now a days phones have lots of internal storage.
Like facebook store more then 500MB images and videos over the time. This library will auto delete images if image being inactive for a given time.



##Usage

please use jitpack

```
    allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

###Gradle

```groovy
dependencies {
    implementation 'com.github.ShahadatShaki:CacheImage:1.1'
}
```

Before loading images. use below code, 

```
ImageCache.setupCacheImage(context, 1000 * 60 * 60 * 24 * 7) //1000 * 60 * 60* 24 * 7 = 7 Days, If any image dose not get loaded within 7 days, That image will removed from cache.
```

Finally, Build a Picasso object then call method. With this you will have all picasso control.

```
val picasso = Picasso.get()
		.load(images[p])
		.placeholder(R.drawable.ic_place_image)
		.error(R.drawable.ic_place_image)

ImageCache.loadAndStoreImage(picasso, imageView, url)
```

You are all done. 
