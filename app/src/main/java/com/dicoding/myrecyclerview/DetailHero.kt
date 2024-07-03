package com.dicoding.myrecyclerview

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.google.gson.Gson

class DetailHero : AppCompatActivity() {
    companion object {
        const val extraname = "EXTRA_NAME"
        const val extradesc = "EXTRA_DESC"
        const val extrarole = "EXTRA_ROLE"
        const val extraorigin = "EXTRA_ORIGIN"
        const val extrapic = "EXTRA_PIC"
        const val extrabg = "EXTRA_BG"
        const val extraVideo = "EXTRA_VIDEO"
        const val extrasound = "EXTRA_SOUND"
        const val extrasvidiodeskripsi = "EXTRA_VIDIODESKRIPSI"
        const val extratamnel = "EXTRA_TAMNEL"
    }
    private var mMediaPlayer: MediaPlayer? = null
    private var isReady: Boolean = false
    private var seekBar: SeekBar? = null
    private var playButton: ImageButton? = null
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var playerView: PlayerView
    private var player: ExoPlayer? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_hero)  // Pastikan ini dijalankan terlebih dahulu

        val buttonMark = findViewById<ImageView>(R.id.mark)
        buttonMark.setOnClickListener {
            val hero = intent.getStringExtra("EXTRA_NAME") ?: ""
            if (isHeroAlreadyMark(hero)) {
                removeHero(hero)
                Toast.makeText(this,"Info Hero dihapus dari mark", Toast.LENGTH_SHORT).show()
            } else {
                markHero()
            }
        }
        val intentData = intent
        // Initialize MediaPlayer
        val soundResId = intentData.getIntExtra(extrasound, 0)
        if (soundResId != 0) {
            mMediaPlayer = MediaPlayer.create(this, soundResId)
            mMediaPlayer?.setOnPreparedListener {
                isReady = true
                seekBar?.max = mMediaPlayer?.duration ?: 0
                tvEndTime.text = formatTime(mMediaPlayer?.duration ?: 0)
            }
            mMediaPlayer?.setOnCompletionListener {
                playButton?.setImageResource(R.drawable.ic_play_circle)
                mMediaPlayer?.seekTo(0)
                seekBar?.progress = 0
                tvStartTime.text = formatTime(0)
            }
        }
        // Initialize UI elements
        seekBar = findViewById(R.id.seekBar)
        playButton = findViewById(R.id.playButton)
        tvStartTime = findViewById(R.id.tvStartTime)
        tvEndTime = findViewById(R.id.tvEndTime)

        // Play button click listener
        playButton?.setOnClickListener {
            if (mMediaPlayer != null) {
                if (!isReady) {
                    mMediaPlayer?.prepareAsync()
                } else {
                    if (mMediaPlayer?.isPlaying == true) {
                        mMediaPlayer?.pause()
                        playButton?.setImageResource(R.drawable.ic_play_circle)
                    } else {
                        mMediaPlayer?.start()
                        playButton?.setImageResource(R.drawable.ic_pause_circle)
                        updateSeekBar()
                    }
                }
            } else {
                Toast.makeText(this, "Sound tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mMediaPlayer?.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        playerView = findViewById(R.id.hero_video)
        initializePlayer()

        val actionbar = supportActionBar
        actionbar!!.title = "Detail"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra(extraname)
        val desc = intent.getStringExtra(extradesc)
        val role = intent.getStringExtra(extrarole)
        val origin = intent.getStringExtra(extraorigin)
        val videodesc = intent.getStringExtra(extrasvidiodeskripsi)
        val pic = intent.getIntExtra(extrapic, 0)
        val picbg = intent.getIntExtra(extrabg, 0)

        val nametext: TextView = findViewById(R.id.hero_name)
        val desctext: TextView = findViewById(R.id.hero_desc)
        val roletext: TextView = findViewById(R.id.hero_role)
        val origintext: TextView = findViewById(R.id.hero_origin)
        val heropic: ImageView = findViewById(R.id.hero_pic)
        val herobg: ImageView = findViewById(R.id.hero_bg)
        val descvideo : TextView = findViewById(R.id.hero_video_desc)

        heropic.setImageResource(pic)
        herobg.setImageResource(picbg)

        roletext.text = role
        origintext.text = origin
        nametext.text = name
        desctext.text = desc
        descvideo.text = videodesc

    }

    private fun markHero() {
        val hero = Hero(
            name = intent.getStringExtra("EXTRA_NAME")?: "",
            description = intent.getStringExtra("EXTRA_DESC")?: "",
            photo = intent.getIntExtra("EXTRA_PIC",0),
            role = intent.getStringExtra("EXTRA_ROLE")?:"",
            origin = intent.getStringExtra("EXTRA_ORIGIN")?: "",
            bg = intent.getIntExtra("EXTRA_BG", 0),
            tamnel = intent.getStringExtra("EXTRA_TAMNEL")?:"",
            deskripsividio = intent.getStringExtra("EXTRA_VIDIODESKRIPSI")?:"",
            videoResId = intent.getIntExtra("EXTRA_SOUND",0)
        )

        val videoHero = intent.getStringExtra("VIDEO_URL") ?: ""

        if(isHeroAlreadyMark(hero.name)) {
            Toast.makeText(this,"Mark Hero sudah terpasang", Toast.LENGTH_SHORT).show()
        }else {
            MarkHeroStorage(hero, videoHero)
            Toast.makeText(this, "Info Hero berhasil dimark", Toast.LENGTH_SHORT).show()
        }
    }

    private fun MarkHeroStorage(hero: Hero, videoHero: String) {
        val sharedPreferences = getSharedPreferences("mark_hero", MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(hero)
        edit.putString(hero.name, json)
        edit.apply()

        val vidShared = getSharedPreferences("mark_video", MODE_PRIVATE)
        val videoEdit = vidShared.edit()
        videoEdit.putString(hero.name, videoHero)
        videoEdit.apply()
    }

    private fun removeHero(hero: String){
        val sharedPreferences = getSharedPreferences("mark_hero", MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.remove(hero)
        edit.apply()
    }

    private fun isHeroAlreadyMark(hero: String): Boolean {
        val sharedPreferences = getSharedPreferences("mark_hero", MODE_PRIVATE)
        return sharedPreferences.contains(hero)
    }
    private fun updateSeekBar() {
        mMediaPlayer?.let {
            seekBar?.progress = it.currentPosition
            tvStartTime.text = formatTime(it.currentPosition)
            if (it.isPlaying) {
                handler.postDelayed({ updateSeekBar() }, 1000)
            }
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.release()
        mMediaPlayer = null
        handler.removeCallbacksAndMessages(null)
        player?.release()
        player = null
    }

    @OptIn(UnstableApi::class) private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val videoUrl = intent.getStringExtra("VIDEO_URL") ?: return
        val mediaSource = buildMediaSource(Uri.parse(videoUrl))
        player?.setMediaSource(mediaSource)
        player?.prepare()
        player?.playWhenReady = true
    }

    @OptIn(UnstableApi::class) private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(this, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri))
    }
}