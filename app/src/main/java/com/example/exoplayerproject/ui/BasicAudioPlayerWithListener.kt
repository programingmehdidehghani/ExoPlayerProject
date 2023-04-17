package com.example.exoplayerproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.exoplayerproject.R
import com.example.exoplayerproject.databinding.ActivityBasicAudioPlayerWithListenerBinding
import com.example.exoplayerproject.util.Constants
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_basic_audio_player_with_listener.*

class BasicAudioPlayerWithListener : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null

    private lateinit var exoPlayerListener: ExoPlayerListener
    private lateinit var eventLogger: EventLogger

    private var playPauseState = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    private val viewBinding by lazy {
        ActivityBasicAudioPlayerWithListenerBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        exoPlayerListener = ExoPlayerListener()
        eventLogger = EventLogger(null)
    }

    private fun initializeExoPlayer(){
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer?.let {
            viewBinding.basicAudioPlayerWithListenerPlayerView.player = exoPlayer
            val mediaSource = buildMediaSource(Constants.MP3_URL)
            it.setMediaSource(mediaSource)
            it.playWhenReady = playPauseState
            it.seekTo(currentWindow,playbackPosition)
            it.addListener(exoPlayerListener)
            it.addAnalyticsListener(eventLogger)
            it.prepare()
        }
    }

    private fun buildMediaSource(url: String): MediaSource{
        val dataSourceFactory = DefaultDataSource.Factory(this)
        return ProgressiveMediaSource.Factory(
            dataSourceFactory
        ).createMediaSource(MediaItem.fromUri(url))
    }

    private fun releasePlayer(){
        exoPlayer?.run {
            this.removeListener(exoPlayerListener)
            this.removeAnalyticsListener(eventLogger)
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playPauseState = this.playWhenReady
            release()
        }
        exoPlayer = null
    }

    inner class ExoPlayerListener : Player.Listener{

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            super.onPlaybackParametersChanged(playbackParameters)
        }

        override fun onSeekProcessed() {
            super.onSeekProcessed()
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
            super.onTracksChanged(trackGroups, trackSelections)
        }

        override fun onLoadingChanged(isLoading: Boolean) {
            super.onLoadingChanged(isLoading)
        }

        override fun onPositionDiscontinuity(reason: Int) {
            super.onPositionDiscontinuity(reason)
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            super.onShuffleModeEnabledChanged(shuffleModeEnabled)
        }

        override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
            super.onPlaybackSuppressionReasonChanged(playbackSuppressionReason)
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            super.onTimelineChanged(timeline, reason)
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)
            when(playbackState){
                ExoPlayer.STATE_IDLE -> {
                    basic_audio_player_with_listener_progress_view_text_view.text = "STATE_IDLE"
                }
                ExoPlayer.STATE_BUFFERING ->{
                    basic_audio_player_with_listener_progress_view_text_view.text =
                        "STATE_BUFFERING"
                    basic_audio_player_with_listener_progress_view.visibility = View.VISIBLE
                }
                ExoPlayer.STATE_READY -> {
                    basic_audio_player_with_listener_progress_view_text_view.text = "STATE_READY"
                    basic_audio_player_with_listener_progress_view.visibility = View.INVISIBLE
                }
                ExoPlayer.STATE_ENDED -> {
                    basic_audio_player_with_listener_progress_view_text_view.text = "STATE_ENDED"
                }
                else -> {
                    basic_audio_player_with_listener_progress_view_text_view.text = "UNKNOWN"
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24){
            initializeExoPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24){
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24){
            releasePlayer()
        }
    }
}