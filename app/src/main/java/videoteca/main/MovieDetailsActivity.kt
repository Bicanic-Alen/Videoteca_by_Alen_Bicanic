package videoteca.main

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import videoteca.main.Adapters.ActorsAdapter
import videoteca.main.Adapters.FilmAdapter
import videoteca.main.Adapters.FilmRaccommendedAdapter
import videoteca.main.Adapters.GenreAdapterMovieDetails

import videoteca.main.Domain.Movie.LogoSize
import videoteca.main.Domain.Movie.PosterSize
import videoteca.main.api.TMDB_ImageManager
import videoteca.main.api.TMDB_Manager
import java.util.Locale
import kotlin.properties.Delegates


class MovieDetailsActivity : AppCompatActivity() {

    private val tmdbManager = TMDB_Manager()
    private val tmdbImagemanager = TMDB_ImageManager()
    private var idMovie by Delegates.notNull<Int>()
    private var TAG = "MovieDetailsActivity"
    private val currentLocale: Locale = Locale.getDefault()

    private lateinit var tvTitle:TextView
    private lateinit var backdropMovie: ImageView
    private lateinit var posterMovie: ImageView
    private lateinit var logoTitleMovie: ImageView
    private lateinit var tvRatingMovie: TextView
    private lateinit var tvDurationMovie: TextView
    private lateinit var tvPlot: TextView
    private lateinit var tvLike: TextView
    private lateinit var btnRent: Button
    private lateinit var recyclerViewActors: RecyclerView
    private lateinit var recyclerViewGenres: RecyclerView
    private lateinit var recyclerViewRecommendations: RecyclerView
    private lateinit var adapterActors: ActorsAdapter
    private lateinit var adapterRecommended:RecyclerView.Adapter<*>
    private lateinit var adapterGenres:RecyclerView.Adapter<*>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_details)
        idMovie = intent.getIntExtra("id", 0)
        
        Log.d(TAG, "id movie: $idMovie")

        initView()

        val language = currentLocale.language //recupera la lingua del dispositivo (es. "it")
        val languageTag = currentLocale.toLanguageTag()

        val toolbarDetails = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarDetails)
        setSupportActionBar(toolbarDetails)
        toolbarDetails.setNavigationOnClickListener {

            onBackPressed() // Per tornare all'activity precedente
        }

        recyclerViewActors = findViewById<RecyclerView>(R.id.recyclerViewActors)
        recyclerViewActors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        recyclerViewRecommendations = findViewById(R.id.recyclerViewRecommendations)
        recyclerViewRecommendations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        adapterActors = ActorsAdapter(emptyList())
        recyclerViewActors.adapter = adapterActors

        adapterRecommended = FilmAdapter(emptyList())
        recyclerViewRecommendations.adapter = adapterRecommended


        recyclerViewGenres = findViewById(R.id.recyclerViewGenres)
        recyclerViewGenres.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterGenres = GenreAdapterMovieDetails(emptyList())
        recyclerViewGenres.adapter = adapterGenres


        tmdbManager.getMovieDetails(idMovie, languageTag) { movieDetails ->
            this.runOnUiThread {
                if (movieDetails != null) {

                    adapterGenres = GenreAdapterMovieDetails(movieDetails.genres)
                    recyclerViewGenres.adapter = adapterGenres

                    //estrago le informazioni per il logo del film
                    var logoPath:String = ""
                    tmdbManager.getMovieImage(idMovie, language) { imageResponse ->
                        this.runOnUiThread { //entro del thread pricipale
                            if (imageResponse != null) {

                                if(imageResponse.logos.isNotEmpty()){
                                    logoPath = tmdbImagemanager.buildImageUrl(LogoSize.W500, imageResponse.logos[0].filePath)
                                    Log.d(TAG,"logo full path $logoPath")
                                    Glide.with(this)
                                        .load(logoPath)
                                        .into(logoTitleMovie)
                                } else { //nel caso non si trovi il logo della lingua corrente si prende in quello in lingua inglese
                                    tmdbManager.getMovieImage(idMovie, "en") { it ->
                                        this.runOnUiThread {
                                            if (it != null && it.logos.isNotEmpty()) {
                                                for (l in it.logos) {
                                                    Log.d(TAG, "sono nel for: ${l.filePath}")
                                                    val filePath = l.filePath
                                                    if (filePath.endsWith(".png")||filePath.endsWith(".jpg")) {
                                                        Log.d(TAG, "sono nel if del for: ${l.filePath}")
                                                        // Se il percorso non termina con ".svg" ma termina con ".png" o ".jpg"
                                                        logoPath = tmdbImagemanager.buildImageUrl(
                                                            LogoSize.W500, filePath)
                                                        Log.d(TAG,"logo full path $logoPath")
                                                        Glide.with(this)
                                                            .load(logoPath)
                                                            .into(logoTitleMovie)
                                                        break
                                                    }
                                                }


                                            }
                                            else{ //altrimenti si setta il titolo con una textview
                                                if(movieDetails.title!=""){
                                                    tvTitle.text = movieDetails.originalTitle
                                                    tvTitle.visibility= View.VISIBLE
                                                }else{
                                                    tvTitle.text = movieDetails.title
                                                    tvTitle.visibility= View.VISIBLE
                                                }
                                            }
                                        }
                                    }
                                }

                                Log.e(TAG, "Success to fetch Images")
                            } else {
                                Log.e(TAG, "Failed to fetch Images ")
                            }
                        }
                    }



                    //recupero backdrop per imagine di sfondo
                    Glide.with(this)
                        .load(tmdbImagemanager.buildImageUrl(PosterSize.W780, movieDetails.backdropPath))
                        .transition(DrawableTransitionOptions.withCrossFade()) // Opzionale: animazione del caricamento
                        .into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                // Sovrappone il Drawable per la sfocatura all'immagine
                                val shadowDrawable = ContextCompat.getDrawable(this@MovieDetailsActivity, R.drawable.gradient_shadow)
                                val layerDrawable = LayerDrawable(arrayOf(resource, shadowDrawable))
                                backdropMovie.setImageDrawable(layerDrawable)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                // Gestione della pulizia
                            }
                        })


                    //recupero poster per il poster del film
                    Glide.with(this)
                        .load(
                            tmdbImagemanager.buildImageUrl(
                                PosterSize.W500,
                                movieDetails.posterPath
                            )
                        )
                        .transform(CenterCrop(), RoundedCorners(30))
                        .into(posterMovie)
                    Log.d(TAG, tmdbImagemanager.buildImageUrl(
                        PosterSize.W500,
                        movieDetails.posterPath
                    ))

                    tvDurationMovie.text = "${movieDetails.runtime} min"
                    tvRatingMovie.text = limitToOneDecimalPlace(movieDetails.voteAverage)
                    if(movieDetails.overview == ""){
                            tmdbManager.getMovieDetails(idMovie,"en-US"){ it->
                                this.runOnUiThread {
                                    if (it!=null){
                                        tvPlot.text= it.overview
                                    }
                                }
                            }

                    }else{
                        tvPlot.text= movieDetails.overview
                    }


                    tmdbManager.getMovieCredits(idMovie, languageTag){ creditsMovie ->
                        this.runOnUiThread{
                            if (creditsMovie!=null){
                                Log.d(TAG, creditsMovie.cast.get(0).name)

                                adapterActors = ActorsAdapter(creditsMovie.cast)
                                Log.d(TAG, "ho creato l'adapter")
                                recyclerViewActors.adapter = adapterActors
                                Log.d(TAG, "success fetch data creditMovies")

                            }else{
                                Log.d(TAG, "error fetch data creditMovies")
                            }
                        }

                    }


                    tmdbManager.getMovieRecommendations(idMovie, languageTag){ it ->
                        this.runOnUiThread{
                            if(it!=null){
                                Log.d(TAG, it.toString())

                                adapterRecommended = FilmRaccommendedAdapter(it.results)
                                Log.d(TAG, "ho creato l'adapter")
                                recyclerViewRecommendations.adapter = adapterRecommended
                                Log.d(TAG, "success fetch data recommended movie")
                            }else{
                                val tvRaccommended = findViewById<TextView>(R.id.tv_raccommended)
                                tvRaccommended.visibility = View.GONE
                                recyclerViewRecommendations.visibility = View.GONE
                                Log.d(TAG, "error fetch data recommended movie")
                            }
                        }
                    }
                }
            }

        }


    }
    private fun initView() {
        tvTitle =findViewById(R.id.tv_title_details)
        tvTitle.visibility = View.INVISIBLE
        backdropMovie = findViewById<ImageView>(R.id.iv_backdrop_details)
        posterMovie = findViewById<ImageView>(R.id.iv_poster_details)
        logoTitleMovie = findViewById<ImageView>(R.id.iv_logo_movie_detail)
        tvRatingMovie = findViewById<TextView>(R.id.tv_rate_details)
        tvDurationMovie = findViewById<TextView>(R.id.tv_movie_duration_details)
        tvPlot = findViewById<TextView>(R.id.tv_summary_content_details)
        tvLike = findViewById<TextView>(R.id.tv_like_details)
        btnRent = findViewById<Button>(R.id.btn_rent)





        recyclerViewGenres = findViewById<RecyclerView>(R.id.recyclerViewGenres)
        recyclerViewGenres.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    fun limitToOneDecimalPlace(number: Double): String {
        // Utilizza String.format con il modello "%.1f" per formattare il double con una cifra decimale
        return String.format("%.1f", number)
    }
}