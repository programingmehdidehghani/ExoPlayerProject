package com.example.exoplayerproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.exoplayerproject.R
import com.example.exoplayerproject.databinding.ActivityBasicVideoPlayerWithSmoothStreamingBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class BasicVideoPlayerWithSmoothStreaming : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var exoPlayerListener: ExoPlayerListener

    private var playPauseState = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    private val viewBinding by lazy {
        ActivityBasicVideoPlayerWithSmoothStreamingBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        exoPlayerListener = ExoPlayerListener()
    }

    private fun initializeExoPlayer(){

    }

    private fun buildMediaSource(url: String): MediaSource{
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        return SsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(url))
    }

    private fun releasePlayer(){
        
    }
}