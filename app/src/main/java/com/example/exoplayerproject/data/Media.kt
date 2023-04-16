package com.example.exoplayerproject.data

import android.net.Uri

data class Media(
    var uri: Uri,
    var mediaId: String,
    var title: String,
    var description: String,
    var bitmapResource: String
)
