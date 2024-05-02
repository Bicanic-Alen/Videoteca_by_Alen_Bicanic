package videoteca.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import org.w3c.dom.Text
import videoteca.main.Adapters.RentedMovieAdapter
import videoteca.main.Domain.Movie.PosterSize
import videoteca.main.api.AuthService
import videoteca.main.api.DatabaseManager
import videoteca.main.api.TMDB_ImageManager
import videoteca.main.api.TMDB_Manager
import java.util.Locale
import kotlin.properties.Delegates

class RentConfirmActivity : AppCompatActivity() {
    private val tmdbManager = TMDB_Manager()
    private val tmdbImagemanager = TMDB_ImageManager()
    private var idMovie by Delegates.notNull<Int>()
    private var TAG = "RentConfirmActivity"
    private val currentLocale: Locale = Locale.getDefault()
    private val languageTag = currentLocale.toLanguageTag()
    private val db = DatabaseManager()
    private val utenteId = AuthService.getCurrentUser()?.uid


    private lateinit var btnConfirm:Button
    private lateinit var title:TextView
    private lateinit var orTitle:TextView
    private lateinit var datePub:TextView
    private lateinit var backdropMovie:ImageView
    private lateinit var poster:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rent_confirm)

        val idMovie = intent.getIntExtra("idMovie", 0)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarConfirmRent)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        btnConfirm = findViewById(R.id.btn_confirm)
        title = findViewById(R.id.tv_title_rent_conf)
        orTitle = findViewById(R.id.tv_or_title_conf)
        datePub = findViewById(R.id.tv_date_conf)
        backdropMovie = findViewById(R.id.iv_backdrop_conf)
        poster = findViewById(R.id.iv_poster_rented_conf)



        if (utenteId != null) {
            btnConfirm.setOnClickListener {
                db.addRentedMovies(utenteId, idMovie)
                SharedInfo(this).saveMovieTime(idMovie, 0L)
                Toast.makeText(this,
                    getString(R.string.movie_rented_successfully_you_can_find_it_in_your_rented_movies_catalog), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent)
            }

        }
        if(currentLocale.language == "en"){
            orTitle.visibility = View.GONE
        }


        tmdbManager.getMovieDetails(idMovie, languageTag){ movieDetails ->
            this.runOnUiThread {
                if (movieDetails!=null){
                    //recupero backdrop per imagine di sfondo
                    Glide.with(this)
                        .load(
                            tmdbImagemanager.buildImageUrl(
                                PosterSize.W780,
                                movieDetails.backdropPath
                            )
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                // Sovrappone il Drawable per la sfocatura all'immagine
                                val shadowDrawable = ContextCompat.getDrawable(
                                    this@RentConfirmActivity,
                                    R.drawable.gradient_shadow
                                )
                                val layerDrawable = LayerDrawable(arrayOf(resource, shadowDrawable))
                                backdropMovie.setImageDrawable(layerDrawable)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                //TMCH
                            }
                        })

                    Glide.with(this)
                        .load(
                            tmdbImagemanager.buildImageUrl(
                                PosterSize.W500,
                                movieDetails.posterPath
                            )
                        )
                        .transform(CenterCrop(), RoundedCorners(30))
                        .into(poster)
                    Log.d(TAG, tmdbImagemanager.buildImageUrl(PosterSize.W500, movieDetails.posterPath))

                    title.text = movieDetails.title
                    orTitle.text = movieDetails.originalTitle
                    datePub.text = movieDetails.releaseDate
                }
            }
        }

    }


}