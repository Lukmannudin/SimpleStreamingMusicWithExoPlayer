package com.codelabs.streamingmusicwithexoplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var exoPlayer: SimpleExoPlayer
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeExoPlayer()

        btn_music.setOnClickListener {
            if (isPlaying){
                btn_music.text = "PLAY MUSIC"
                pauseMusic()
            } else {
                btn_music.text = "PAUSE MUSIC"
                playMusic()
            }
        }
    }

    fun initializeExoPlayer(){
        @C.AudioUsage val usage = Util.getAudioUsageForStreamType(C.STREAM_TYPE_MUSIC)
        @C.AudioContentType val contentType =
                Util.getAudioContentTypeForStreamType(C.STREAM_TYPE_MUSIC)

        val audioAttributes =
            com.google.android.exoplayer2.audio.AudioAttributes.Builder().setUsage(usage)
                .setContentType(contentType)
                .build()


        val dateSourceFactory = DefaultDataSourceFactory(
                this,
                Util.getUserAgent(this, packageName),
                DefaultBandwidthMeter() as TransferListener
        )

        val mediaSource = ExtractorMediaSource(
                Uri.parse("https://firebasestorage.googleapis.com/v0/b/testmyproject-2afdc.appspot.com/o/AJ%20Salvatore%20Fluencee%20-%20Better%20(Lyrics)%20AJ%20Salvatore%20Remix%20feat.%20Bri%20Tolani.mp3?alt=media&token=adc0c0cb-2da3-4614-81e2-79c0eadaee5f"),
                dateSourceFactory,
                DefaultExtractorsFactory(),
                Handler(),
                ExtractorMediaSource.EventListener {
                    it.printStackTrace()
                }
        )

        val bandwidthMeter = DefaultBandwidthMeter()
        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        exoPlayer =
                ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector(trackSelectionFactory))

        exoPlayer.prepare(mediaSource)
        exoPlayer.audioAttributes = audioAttributes
        exoPlayer.volume = 1f

    }

    fun playMusic(){
        exoPlayer.playWhenReady = true
        isPlaying = true
    }

    fun pauseMusic(){
        exoPlayer.playWhenReady = false
        isPlaying = false
    }
}
