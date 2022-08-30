package com.shaki.image_cache

import java.io.Serializable

class ImageFileInfo : Serializable {
    var fileName: String = ""
    var lastOpened: Long = 0
}