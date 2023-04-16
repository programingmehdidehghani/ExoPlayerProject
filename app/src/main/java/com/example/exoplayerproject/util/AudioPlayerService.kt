package com.example.exoplayerproject.util

import android.app.Service
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player

class AudioPlayerService: LifecycleService(), Player.Listener {

    private var exoPlayer: ExoPlayer? = null

    private var songTitleLiveData = MutableLiveData<String>()
    private var songDescriptionLiveData = MutableLiveData<String>()
    private var songIconLiveData = MutableLiveData<Int>()

    
}