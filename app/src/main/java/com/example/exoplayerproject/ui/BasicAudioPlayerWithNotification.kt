package com.example.exoplayerproject.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.exoplayerproject.R
import com.example.exoplayerproject.util.AudioPlayerService
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_basic_audio_player_with_notification.*

class BasicAudioPlayerWithNotification : AppCompatActivity() {

    private lateinit var intentService: Intent
    private lateinit var serviceBinder: AudioPlayerService.AudioPlayerServiceBinder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_audio_player_with_notification)

        intentService = Intent(this, AudioPlayerService::class.java)
        Util.startForegroundService(this,intentService)
    }

    private val serviceConnector =
              object: ServiceConnection {
                  override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
                      if (service is AudioPlayerService.AudioPlayerServiceBinder){
                           serviceBinder = service

                          basic_audio_player_with_notification_player_view.player =
                              serviceBinder.getSimpleExoPlayerInstance()

                          serviceBinder.getTitleLiveData()
                              .observe(this@BasicAudioPlayerWithNotification, Observer {
                                  basic_audio_player_with_notification_text_view.text = it
                              })

                          serviceBinder.getIconLiveData()
                              .observe(this@BasicAudioPlayerWithNotification, Observer {
                                  basic_audio_player_with_notification_text_view_image_view.setImageDrawable(
                                      ContextCompat.getDrawable(this@BasicAudioPlayerWithNotification,it)
                                  )
                              })
                      }
                  }

                  override fun onServiceDisconnected(p0: ComponentName?) {
                  }
              }

    override fun onStart() {
        super.onStart()
        bindService(intentService,serviceConnector,Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        unbindService(serviceConnector)
        super.onStop()
    }
}