package com.example.exoplayerproject.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.exoplayerproject.R
import com.example.exoplayerproject.ui.BasicAudioPlayerWithNotification
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

@Suppress("DEPRECATION")
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

        for (sample in Constants.MP3_SAMPLE_PLAYLIST){
            val progressiveMediaSource =
                ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(sample.uri))
            concatenatingMediaSource.addMediaSource(progressiveMediaSource)
        }

        exoPlayer?.let {
            it.addListener(this)
            it.setMediaSource(concatenatingMediaSource)
            it.prepare()
            it.playWhenReady = true
        }
        setupNotification(context)
    }

    private fun setupNotification(context: AudioPlayerService){
        playerNotificationPlayer = PlayerNotificationManager.Builder(
            context,
            Constants.PLAYBACK_NOTIFICATION_ID, Constants.PLAYBACK_CHANNEL_ID
        ).setChannelImportance(R.string.playback_channel_name)
            .setChannelDescriptionResourceId(R.string.playback_channel_description)
            .setMediaDescriptionAdapter(object: PlayerNotificationManager.MediaDescriptionAdapter{

                @SuppressLint("UnspecifiedImmutableFlag")
                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    val intent = Intent(context, BasicAudioPlayerWithNotification::class.java)
                    return PendingIntent.getActivities(
                        context,
                        0,
                        arrayOf(intent),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                override fun getCurrentContentText(player: Player): CharSequence? {
                    songDescriptionLiveData.postValue(Constants.MP3_SAMPLE_PLAYLIST[player.currentMediaItemIndex].description)
                    return Constants.MP3_SAMPLE_PLAYLIST[player.currentMediaItemIndex].description
                }

                override fun getCurrentContentTitle(player: Player): CharSequence {
                    songTitleLiveData.postValue(Constants.MP3_SAMPLE_PLAYLIST[player.currentMediaItemIndex].title)
                    return Constants.MP3_SAMPLE_PLAYLIST[player.currentMediaItemIndex].title
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    songIconLiveData.postValue(Constants.MP3_SAMPLE_PLAYLIST[player.currentMediaItemIndex].bitmapResource)
                    return Constants.getBitmap(
                        context,
                        Constants.MP3_SAMPLE_PLAYLIST[player.currentMediaItemIndex].bitmapResource
                    )
                }
            })
            .setNotificationListener(playerNotificationListener)
            .build()
    }

    private var playerNotificationListener: PlayerNotificationManager.NotificationListener =
        object: PlayerNotificationManager.NotificationListener{

            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                super.onNotificationCancelled(notificationId, dismissedByUser)
                stopSelf()
            }

            override fun onNotificationPosted(
                notificationId: Int,
                notification: Notification,
                ongoing: Boolean
            ) {
                super.onNotificationPosted(notificationId, notification, ongoing)
                if (ongoing){
                    startForeground(notificationId,notification)
                } else{
                    stopForeground(false)
                }
            }
        }

    override fun onDestroy() {
        exoPlayer?.let {
            playerNotificationPlayer?.let {
                it.setPlayer(null)
                playerNotificationPlayer = null
            }
            it.removeListener(this)
            it.release()
            exoPlayer = null
        }
        super.onDestroy()
    }

    inner class AudioPlayerServiceBinder: Binder(){
        fun getSimpleExoPlayerInstance() = exoPlayer

        fun getTitleLiveData() = songTitleLiveData
        fun getDescriptionLiveData() = songDescriptionLiveData

        fun getIconLiveData() = songIconLiveData
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}