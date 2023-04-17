package com.example.exoplayerproject.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.exoplayerproject.R
import com.example.exoplayerproject.util.Constants
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_basic_audio_player_with_playlist.*

class BasicMediaPlayerWithPlaylist : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var exoPlayerListener: ExoPlayerListener

    private var playPauseState = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_audio_player_with_playlist)
        exoPlayerListener = ExoPlayerListener()
    }

    private fun initializeExoPlayer(){
       exoPlayer = ExoPlayer.Builder(this).build()
       exoPlayer?.let {
           it.addListener(exoPlayerListener)
           basic_audio_player_with_playlist_player_view.player = exoPlayer
           val mediaSource = buildMediaSource(Constants.MP3_URL, Constants.MP4_URL)
           it.setMediaSource(mediaSource)
           it.playWhenReady = playPauseState
           it.seekTo(currentWindow,playbackPosition)
           it.prepare()
       }
    }

    private fun buildMediaSource(url1: String,url2: String): MediaSource{

        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaSource1 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(url1))
        val mediaSource2 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(url2))

        return ConcatenatingMediaSource(mediaSource1,mediaSource2,mediaSource1,mediaSource2)
    }

    private fun releasePlayer(){
        exoPlayer?.run {
            removeListener(exoPlayerListener)
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playPauseState = this.playWhenReady
            release()
        }
        exoPlayer = null
    }

    inner class ExoPlayerListener : Player.Listener {
        @SuppressLint("SetTextI18n")
        override fun onPositionDiscontinuity(reason: Int) {
            basic_audio_player_with_playlist_text_view.text =
                "reason = ${reason}"
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
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