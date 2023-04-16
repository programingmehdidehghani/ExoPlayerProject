package com.example.exoplayerproject.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.exoplayerproject.R
import com.example.exoplayerproject.databinding.ActivityBasicAudioPlayerWithHlsmediaSourceBinding
import com.example.exoplayerproject.util.Constants
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsManifest
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_basic_audio_player_with_hlsmedia_source.*

@Suppress("DEPRECATION")
class BasicAudioPlayerWithHLSMediaSource : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var exoPlayerListener: ExoPlayerListener

    private var playPauseState = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    private val viewBinding by lazy (LazyThreadSafetyMode.NONE){
        ActivityBasicAudioPlayerWithHlsmediaSourceBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        exoPlayerListener = ExoPlayerListener()
    }

    private fun initializeExoPlayer(){
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer?.let {
            viewBinding.basicAudioPlayerWithHlsMediaSourcePlayerView.player = exoPlayer
            val mediaSource = buildMediaSource(Constants.HLS_URL)
            it.setMediaSource(mediaSource)
            it.playWhenReady = playPauseState
            it.seekTo(currentWindow,playbackPosition)
            it.prepare()
        }

    }

    private fun buildMediaSource(url: String): MediaSource{
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Constants.uriParser(url)))
    }

    private fun releasePlayer(){
        exoPlayer?.run {
            playbackPosition = this.contentPosition
            currentWindow = this.currentMediaItemIndex
            playPauseState = this.playWhenReady
            release()
        }
        exoPlayer = null
    }

    inner class ExoPlayerListener: Player.Listener{
        @SuppressLint("SetTextI18n")
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            exoPlayer?.let {
                val manifest = it.currentManifest
                manifest?.let {
                    val hlsManifest = it as HlsManifest
                    basic_audio_player_with_hls_media_source_text_view.text=
                        "masterPlaylist = ${hlsManifest.masterPlaylist} \n mediaPlaylist = ${hlsManifest.mediaPlaylist} "
                }
            }
            super.onTimelineChanged(timeline, reason)
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