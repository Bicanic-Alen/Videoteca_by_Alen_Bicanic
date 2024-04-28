package videoteca.main

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope

import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.interfaces.IMedia.Slave.Type.Subtitle
import org.videolan.libvlc.util.VLCVideoLayout
import videoteca.main.Domain.Videoteca
import videoteca.main.api.DatabaseManager
import videoteca.main.api.TMDB_Manager
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates


private const val USE_TEXTURE_VIEW = true
private const val ENABLE_SUBTITLES = true

class MovieStreamActivity : AppCompatActivity() {

    //API
    private val storage = Firebase.storage("gs://videoteca-ec23f.appspot.com")
    private val db = DatabaseManager()
    private var movieid by Delegates.notNull<Int>()
    private val tmdbManager = TMDB_Manager()
    private val TAG = "MovieStreamActivity"
    private val currentLocale: Locale = Locale.getDefault()
    private var areButtonsVisible = false
    private var savedPosition: Long = 0


    //videoplayer
    private lateinit var libVLC: LibVLC
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playerVideo : VLCVideoLayout
    private lateinit var ivPlaypause: ImageView
    private lateinit var ivBack10: ImageView
    private lateinit var ivSkip10: ImageView
    private lateinit var ivCast: ImageView
    private lateinit var ivBackRent: ImageView
    private lateinit var seekBarNavigation: SeekBar
    private lateinit var tvTime : TextView
    private lateinit var tvTitle : TextView
    private lateinit var btnsPlayerLayout: View
    private val hideButtonsDelay = 5000L // 5 secondi
    private var hideButtonsJob: Job? = null


    @OptIn(UnstableApi::class) @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_stream)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val options = arrayListOf<String>(
            "--aout=opensles",
            "--audio-time-stretch",
            "--fullscreen",
            "--no-video-title-show"


        )

        libVLC = LibVLC(this, options)

        mediaPlayer =  MediaPlayer(libVLC)

        movieid = intent.getIntExtra("id", 0)

        playerVideo = findViewById(R.id.player_video)

        btnsPlayerLayout = findViewById(R.id.btns_player)
        tvTitle = findViewById(R.id.tv_title_moviestream)
        ivPlaypause = findViewById(R.id.iv_play_pause)
        ivSkip10 = findViewById(R.id.iv_skip_10)
        ivBack10 = findViewById(R.id.iv_back_10)
        seekBarNavigation = findViewById(R.id.seekBar_timeline)
        ivBackRent = findViewById(R.id.iv_back_page)
        ivCast = findViewById(R.id.iv_cast)
        tvTime = findViewById(R.id.tv_movietime_remmaing)
        seekBarNavigation.progress = 0

        tmdbManager.getMovieDetails(movieid, currentLocale.toLanguageTag()){
            this.runOnUiThread{
                if(it!=null){
                    tvTitle.text = it.title
                }
            }
        }

        hideButtons()


        playerVideo.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (areButtonsVisible) {
                        hideButtons()
                        cancelHideButtonsTimer()
                        areButtonsVisible = false
                    } else {
                        showButtons()
                        startHideButtonsTimer()
                        areButtonsVisible = true
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    cancelHideButtonsTimer()
                }
            }
            true
        }

        lifecycleScope.launch {
            val path = getMoviePath()?.video_path
            val storageRef = Firebase.storage.reference.child("movies$path")

            if (path != null) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val videoUrl = uri.toString()
                    Log.d(TAG, "full url del film $videoUrl")
                    val media = Media(libVLC, Uri.parse(videoUrl))
                    mediaPlayer.media = media
                    media.release()
                    mediaPlayer.attachViews(playerVideo, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW)
                    mediaPlayer.play()

                    seekBarNavigation.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            if (fromUser) {

                                mediaPlayer.time = progress.toLong()
                            }
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })

                    mediaPlayer.setEventListener { event ->
                        when (event?.type) {
                            MediaPlayer.Event.Opening -> {
                                // Il video è in fase di apertura
                                Log.d(TAG, "Video in fase di apertura...")
                            }
                            MediaPlayer.Event.Buffering -> {
                                // Il video è in fase di buffering
                                Log.d(TAG, "Video in fase di buffering...")
                            }
                            MediaPlayer.Event.Playing -> {
                                //il video è in riproduzione
                                Log.d(TAG, "Video pronto per la riproduzione")

                                val duration = mediaPlayer.length
                                if (duration > 0) {

                                    seekBarNavigation.max = duration.toInt()

                                    tvTime.text = millisecondiToTempoString(duration)
                                } else {
                                    Log.e(TAG, "Durata del video non valida: $duration")
                                }
                            }
                            MediaPlayer.Event.TimeChanged -> {
                                seekBarNavigation.progress = mediaPlayer.time.toInt()
                                val remainingTime = mediaPlayer.length - mediaPlayer.time
                                tvTime.text = millisecondiToTempoString(remainingTime)
                            }
                            MediaPlayer.Event.EndReached->{
                                onBackPressed()
                            }
                        }
                    }


                }.addOnFailureListener { exception ->
                    Log.d(TAG, "errore nel recupero del download path", exception)
                }
            } else {
                Log.e(TAG, "errore nel recupero delle informazioni dal database del nome del file da cercare")
            }
        }



        //gestione pulsanti

        ivPlaypause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                ivPlaypause.setImageResource(R.drawable.ic_play_circle_outline)
                mediaPlayer.pause()
            } else {
                ivPlaypause.setImageResource(R.drawable.baseline_pause_circle_outline_24)
                mediaPlayer.play()
            }
        }



        ivSkip10.setOnClickListener {
            mediaPlayer.time += 10000
        }


        ivBack10.setOnClickListener {
            mediaPlayer.time -= 10000
        }


        ivBackRent.setOnClickListener {
            onBackPressed()
        }

    }

    private fun cancelHideButtonsTimer() {
        hideButtonsJob?.cancel()
    }

    private fun startHideButtonsTimer() {
        cancelHideButtonsTimer()
        hideButtonsJob = CoroutineScope(Dispatchers.Main).launch {
            delay(hideButtonsDelay)
            hideButtons()
        }
    }

    private fun showButtons() {
        btnsPlayerLayout.visibility = View.VISIBLE
    }

    private fun hideButtons(){
        btnsPlayerLayout.visibility = View.GONE
    }

    private suspend fun getMoviePath(): Videoteca? {
        return suspendCoroutine { continuation ->
            db.getVidetecaItem(movieid) { videoteca ->
                continuation.resume(videoteca)
            }
        }
    }

    fun millisecondiToTempoString(millisecondi: Long): String {
        val ore = millisecondi / 3600000
        val minuti = (millisecondi % 3600000) / 60000
        val secondi = (millisecondi % 60000) / 1000

        return String.format("%d:%02d:%02d", ore, minuti, secondi)
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            savedPosition = mediaPlayer.time
            mediaPlayer.pause()
            ivPlaypause.setImageResource(R.drawable.ic_play_circle_outline)
        }
    }

    override fun onResume() {
        super.onResume()
        if (savedPosition > 0) {
            mediaPlayer.time = savedPosition
            mediaPlayer.play()
            ivPlaypause.setImageResource(R.drawable.baseline_pause_circle_outline_24)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("savedPosition", savedPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedPosition = savedInstanceState.getLong("savedPosition")
    }


    override fun onDestroy() {
        super.onDestroy()
        cancelHideButtonsTimer()
        mediaPlayer.release()
        libVLC.release()
    }


}