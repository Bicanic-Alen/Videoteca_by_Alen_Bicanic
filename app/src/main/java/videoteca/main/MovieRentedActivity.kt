package videoteca.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import videoteca.main.Adapters.RentedMovieAdapter
import videoteca.main.Domain.MovieRentedInfo
import videoteca.main.Domain.UserDB
import videoteca.main.api.AuthService
import videoteca.main.api.DatabaseManager
import videoteca.main.api.TMDB_Manager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class MovieRentedActivity : AppCompatActivity() {

    private val db = DatabaseManager()
    private val uid = AuthService.getCurrentUser()?.uid
    private val tmdbManager = TMDB_Manager()
    private val currentLocale: Locale = Locale.getDefault()
    private val languageTag = currentLocale.toLanguageTag()
    private val TAG = "MovieRentedActivity"
    private var flagInit = false

    private lateinit var recyclerViewRentedMovies:RecyclerView
    private lateinit var adapterRentedMovies: RentedMovieAdapter
    private lateinit var loading: ProgressBar
    private lateinit var tvAlert:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_rented)

        val statusBarColor = if (isDarkTheme()) {
            ContextCompat.getColor(this, R.color.black)
        } else {
            ContextCompat.getColor(this, R.color.gray_300)
        }

        window.statusBarColor = statusBarColor

        tvAlert = findViewById(R.id.tv_alert_norentmovie)
        recyclerViewRentedMovies = findViewById(R.id.recyclerView_rentedmovies)
        loading = findViewById(R.id.loading_rented)
        loading.visibility = View.VISIBLE

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarRentedMovie)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {

            onBackPressed() // Per tornare all'activity precedente
        }


        recyclerViewRentedMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapterRentedMovies = RentedMovieAdapter(emptyList())
        recyclerViewRentedMovies.adapter = adapterRentedMovies


        db.checkFilmRentedValidity()

        Log.d(TAG, "è gia stato inzializato: $flagInit")
        if(!flagInit) {
            Log.d(TAG, "sono nel if check flag")
            initInfo()
        }

    }




    /**
     * restituisce la data di quando scade un titolo
     */
    private fun getExpirationDate(timestampFirebaseSeconds: Long): Date {
        val rentDate = Date(timestampFirebaseSeconds * 1000) // secondi in millisecondi
        val calendar = Calendar.getInstance()
        calendar.time = rentDate
        calendar.add(Calendar.DAY_OF_YEAR, 7) // + 7 giorni
        return calendar.time
    }

    private fun initInfo(){
        flagInit = true
        //ottengo informazioni per la copertina e titolo e durata, e data di scadenza
        if (uid != null) {
            db.getRentedMovies(uid) { rentedMovies ->
                if(rentedMovies.isNotEmpty()){
                    runOnUiThread{
                        tvAlert.visibility = View.GONE
                    }
                    val listRent: MutableList<MovieRentedInfo> = mutableListOf()

                    for (rented in rentedMovies) {
                        val idMovie = rented.id
                        val dayRent = rented.rentDay?.seconds ?: 0
                        val expirationDate = getExpirationDate(dayRent)

                        tmdbManager.getMovieDetails(idMovie, languageTag) { movieDetails ->
                            var contIdNull = 0;
                            if (movieDetails != null) {
                                if(idMovie == 0) {
                                    contIdNull++
                                }
                                else{
                                    val rentedMovieInfo = MovieRentedInfo(idMovie, expirationDate, movieDetails.posterPath, movieDetails.title, movieDetails.releaseDate)
                                    listRent.add(rentedMovieInfo)
                                }

                                if (listRent.size == rentedMovies.size-contIdNull) {
                                    runOnUiThread {
                                        loading.visibility = View.GONE
                                        adapterRentedMovies = RentedMovieAdapter(listRent)
                                        recyclerViewRentedMovies.adapter = adapterRentedMovies
                                    }
                                }
                            }
                        }
                    }
                }else{
                    runOnUiThread {
                        loading.visibility = View.GONE
                        tvAlert.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun isDarkTheme(): Boolean {
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

}