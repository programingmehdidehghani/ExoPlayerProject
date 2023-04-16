package com.example.exoplayerproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.exoplayerproject.R
import com.google.android.exoplayer2.ExoPlayer
import com.example.exoplayerproject.databinding.ActivityBasicAudioPlayerBinding
import com.example.exoplayerproject.util.Constants
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util


class BasicAudioPlayer : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null

    private var playPauseState = true
    private var currentWindow = 0
    private var playBackPosition = 0L


    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityBasicAudioPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }

    private fun initializeExoPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer?.let {
            viewBinding.basicAudioPlayerPlayerView.player = exoPlayer
            val mediaSource = buildMediaSource(Constants.MP3_URL)
            it.setMediaSource(mediaSource)
            it.playWhenReady = playPauseState
            it.seekTo(currentWindow, playBackPosition)
            it.prepare()
        }
    }

    private fun buildMediaSource(url: String): MediaSource {
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        return ProgressiveMediaSource.Factory(
            dataSourceFactory
        ).createMediaSource(MediaItem.fromUri(Constants.uriParser(url)))
    }

    private fun releasePlayer() {
        exoPlayer?.run {
            playBackPosition = this.contentPosition
            currentWindow = this.currentMediaItemIndex
            playPauseState = this.playWhenReady
            release()
        }
        exoPlayer = null
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24){
            initializeExoPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || exoPlayer == null)) {
            initializeExoPlayer()
        }
    }


    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }
}