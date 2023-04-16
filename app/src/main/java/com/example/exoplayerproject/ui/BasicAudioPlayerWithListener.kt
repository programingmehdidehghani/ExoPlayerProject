package com.example.exoplayerproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.exoplayerproject.R
import com.example.exoplayerproject.databinding.ActivityBasicAudioPlayerWithListenerBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.EventLogger

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
            val mediaSource:
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

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
        }
    }
}