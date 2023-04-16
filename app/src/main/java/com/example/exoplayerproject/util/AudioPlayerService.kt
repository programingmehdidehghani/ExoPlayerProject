package com.example.exoplayerproject.util

import android.app.Service
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class AudioPlayerService: LifecycleService(), Player.Listener {

    private var exoPlayer: ExoPlayer? = null

    private var songTitleLiveData = MutableLiveData<String>()
    private var songDescriptionLiveData = MutableLiveData<String>()
    private var songIconLiveData = MutableLiveData<Int>()

    private var playerNotificationPlayer: PlayerNotificationManager? = null
   // private var binder: AudioPlayerServiceBinder()

    private fun startPlayer(){
        val context = this
        exoPlayer = ExoPlayer.Builder(context).build()
        val defaultDataSourceFactory = DefaultHttpDataSource.Factory()
        val concatenatingMediaSource = ConcatenatingMediaSource()

        for (sample in C)
    }

}