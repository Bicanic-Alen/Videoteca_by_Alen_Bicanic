package videoteca.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
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
    private val db = DatabaseManager()
    private var movieid by Delegates.notNull<Int>()
    private val tmdbManager = TMDB_Manager()


    //DEVICE
    private val TAG = "MovieStreamActivity"
    private val currentLocale: Locale = Locale.getDefault()
    val manufacturer = android.os.Build.MANUFACTURER
    private var areButtonsVisible = false
    private var savedPosition: Long = 0
    private var isStartPositionSet = false

    private var flagBack = false



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
    private lateinit var btnRestart: View
    private lateinit var ivSubLang : ImageView
    private lateinit var loading: ProgressBar

    private var foundAudioTracks = false
    private var foundSubtitleTracls = false



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_stream)

        Log.d(TAG,manufacturer.lowercase(Locale.ROOT))

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        var options = arrayListOf<String>()

        options = arrayListOf<String>(
            "--aout=opensles",
            "--audio-time-stretch",
            "--fullscreen",
            "--no-video-title-show",
        )

        loading = findViewById(R.id.pb_stream)
        loading.visibility = View.VISIBLE

        movieid = intent.getIntExtra("id", 0)


        Log.i(TAG, "posizione film: ${SharedInfo(this).getMovieTime(movieid)}")

        savedPosition = SharedInfo(this).getMovieTime(movieid)








        libVLC = LibVLC(this, options)

        mediaPlayer =  MediaPlayer(libVLC)



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
        btnRestart = findViewById(R.id.cl_restart)
        ivSubLang = findViewById(R.id.iv_sub_lang)
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

                        areButtonsVisible = false
                    } else {
                        showButtons()

                        areButtonsVisible = true
                    }
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
                    savedPosition = SharedInfo(baseContext).getMovieTime(movieid)
                    mediaPlayer.time=savedPosition
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

                    mediaPlayer.time = savedPosition

                    mediaPlayer.setEventListener { event ->
                        when (event?.type) {
                            MediaPlayer.Event.Opening -> {
                                loading.visibility = View.VISIBLE
                                // Il video è in fase di apertura
                                Log.d(TAG, "Video in fase di apertura...")
                            }
                            MediaPlayer.Event.Buffering -> {

                                // Il video è in fase di buffering
                                Log.d(TAG, "Video in fase di buffering...")
                            }
                            MediaPlayer.Event.Playing -> {
                                loading.visibility = View.GONE
                                //il video è in riproduzione
                                Log.d(TAG, "Video pronto per la riproduzione")

                                // Imposta la posizione del video solo se non è già stata impostata
                                if (!isStartPositionSet) {
                                    mediaPlayer.time = savedPosition
                                    isStartPositionSet = true
                                }

                                val duration = mediaPlayer.length
                                if (duration > 0) {

                                    seekBarNavigation.max = duration.toInt()

                                    tvTime.text = millisecondiToTempoString(duration)
                                } else {
                                    Log.e(TAG, "Durata del video non valida: $duration")
                                }
                                if (window.attributes.flags.and(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) == 0) {
                                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                                }

                                var audioTracks:List<MediaPlayer.TrackDescription>
                                audioTracks= emptyList()
                                if( mediaPlayer.audioTracks!=null){
                                    audioTracks = mediaPlayer.audioTracks.toList()
                                    foundAudioTracks = true
                                }
                                var subtitleTracks:List<MediaPlayer.TrackDescription>
                                subtitleTracks = emptyList()
                                if(mediaPlayer.spuTracks != null){
                                    subtitleTracks = mediaPlayer.spuTracks.toList()
                                    foundSubtitleTracls = true
                                }




                                if(foundAudioTracks) {
                                    ivSubLang.setOnClickListener {
                                        showAudioAndSubtitleSelectionDialog(
                                            audioTracks,
                                            subtitleTracks,
                                            mediaPlayer,
                                            { selectedAudio ->
                                                // Imposta la traccia audio selezionata sul MediaPlayer
                                                mediaPlayer.audioTracks.forEachIndexed { index, track ->
                                                    if (track.name == selectedAudio) {
                                                        mediaPlayer.audioTrack = index
                                                        return@forEachIndexed
                                                    }
                                                }
                                                Log.d(TAG, "Audio selezionato: $selectedAudio")
                                            },
                                            { selectedSubtitle ->
                                                if(selectedSubtitle!="") {
                                                    // Imposta i sottotitoli selezionati sul MediaPlayer
                                                    mediaPlayer.spuTracks.forEachIndexed { index, track ->
                                                        if (track.name == selectedSubtitle) {
                                                            mediaPlayer.spuTrack = index
                                                            return@forEachIndexed
                                                        }
                                                    }
                                                }
                                                Log.d(
                                                    TAG,
                                                    "Sottotitoli selezionati: $selectedSubtitle"
                                                )
                                            }
                                        )
                                    }
                                }else{
                                    ivSubLang.visibility = View.INVISIBLE
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

                            MediaPlayer.Event.Paused ->{
                                if (window.attributes.flags.and(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0) {
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                                }
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
                //SharedInfo(this).saveMovieTime(movieid, mediaPlayer.time)
                ivPlaypause.setImageResource(R.drawable.ic_play_circle_outline)
                mediaPlayer.pause()
            } else {
                ivPlaypause.setImageResource(R.drawable.baseline_pause_circle_outline_24)
                mediaPlayer.play()
            }
        }

        btnRestart.setOnClickListener{
            mediaPlayer.time = 0L
        }



        ivSkip10.setOnClickListener {
            mediaPlayer.time += 10000
        }


        ivBack10.setOnClickListener {
            mediaPlayer.time -= 10000
        }


        ivBackRent.setOnClickListener {
            flagBack = true
            onBackPressed()
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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("savedPosition", savedPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedPosition = savedInstanceState.getLong("savedPosition")
    }

    fun showAudioAndSubtitleSelectionDialog(
        audioTrackOptions: List<MediaPlayer.TrackDescription>,
        subtitleOptions: List<MediaPlayer.TrackDescription>,
        mediaPlayer: MediaPlayer,
        onAudioSelected: (String) -> Unit,
        onSubtitleSelected: (String) -> Unit
    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_audio_subtitle_selection, null)

        val audioSpinner: Spinner = dialogView.findViewById(R.id.spinnerAudio)
        val subtitleSpinner: Spinner = dialogView.findViewById(R.id.spinnerSubtitle)

        if(!foundSubtitleTracls){
            subtitleSpinner.isEnabled = false
        }
        var audiolist = mutableListOf<String>()
        for (audio in audioTrackOptions){
            audiolist.add(audio.name)
        }

        mediaPlayer.audioTrack.let { currentAudioTrack ->
            audiolist.removeAt(currentAudioTrack)
            audiolist.add(0, audioTrackOptions[currentAudioTrack].name)
        }

        val audioAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, audiolist)
        audioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        audioSpinner.adapter = audioAdapter

        var subList = mutableListOf<String>()
        if(foundSubtitleTracls){
            for(sub in subtitleOptions){
                subList.add(sub.name)
            }

            mediaPlayer.spuTrack.let { currentSpuTrack ->
                subList.removeAt(currentSpuTrack)
                subList.add(0, subtitleOptions[currentSpuTrack].name)
            }

            val subtitleAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subList)
            subtitleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            subtitleSpinner.adapter = subtitleAdapter
        }


        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setTitle(getString(R.string.select_audio_and_subtitles))
            .setPositiveButton(getString(R.string.conferma)) { dialog, _ ->
                val selectedAudio = audiolist[audioSpinner.selectedItemPosition]
                var selectedSubtitle:String = ""
                if(foundSubtitleTracls){
                    selectedSubtitle = subList[subtitleSpinner.selectedItemPosition]
                }
                onAudioSelected(selectedAudio.toString())
                onSubtitleSelected(selectedSubtitle.toString())

                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.annulla)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "sono in onPause")
        if (mediaPlayer.isPlaying) {
            savedPosition = mediaPlayer.time
            SharedInfo(this).saveMovieTime(movieid,savedPosition)
            Log.i(TAG, "il film è alla time position: $savedPosition")
            mediaPlayer.pause()
            ivPlaypause.setImageResource(R.drawable.ic_play_circle_outline)
        }
        savedPosition = mediaPlayer.time
    }

    override fun onStop() {
        super.onStop()

        when(manufacturer.lowercase(Locale.ROOT)){
            "xiaomi", "redmi"->{
                Log.i(TAG, "sono onStop - il dispositivo è un xiaomi/redmi torno nella pagina dei film nollegiati")
                if(!flagBack){
                    onBackPressed()
                }
            }
            else -> {
                Log.i(TAG, "sono onStop")
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            SharedInfo(this).saveMovieTime(movieid,savedPosition)
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        if (::libVLC.isInitialized) {
            libVLC.release()
        }
        Log.i(TAG, "sono in OnDestroy")
    }


}