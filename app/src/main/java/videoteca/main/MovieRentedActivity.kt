package videoteca.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
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
    private val idu = AuthService.getCurrentUser()?.uid
    private val tmdbManager = TMDB_Manager()
    private val currentLocale: Locale = Locale.getDefault()
    private val languageTag = currentLocale.toLanguageTag()

    private lateinit var recyclerViewRentedMovies:RecyclerView
    private lateinit var adapterRentedMovies: RentedMovieAdapter
    private lateinit var loading: ProgressBar

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


        if (idu != null) {
            //check dei film noleggiati validi e rimozione di quelli non validi
            db.getRentedMovies(idu) { rentedMovies ->
                for (rented in rentedMovies) {
                    val rentDayTimestamp = rented.rentDay?.seconds ?: 0
                    if (checkIfMoreThanSevenDaysPassed(rentDayTimestamp)) {
                        db.removeRentedMovie(idu, rented.id)
                    }
                }
            }

        }
        initInfo()
    }

    /**
     * controlla se dal timestamp fornito sono passati piu di 7 giorni
     * @param timestampFirebaseSeconds richiede il timestamp in secondi nel formato Long
     * @return Boolean, vero se sono passati piu di 7 giorni false altrimenti
     */
    private fun checkIfMoreThanSevenDaysPassed(timestampFirebaseSeconds: Long): Boolean {
        val firebaseDate = Date(timestampFirebaseSeconds * 1000) // Converti secondi in millisecondi
        val currentDate = Date()

        val difference = differenceInDays(firebaseDate, currentDate)
        return difference > 7
    }

    /**
     * calcola la differenza tra le due date
     */

    private fun differenceInDays(date1: Date, date2: Date): Long {
        val diffInMillies = abs(date2.time - date1.time)
        val diffInDays = diffInMillies / (1000 * 60 * 60 * 24)
        return diffInDays
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
        //ottengo informazioni per la copertina e titolo e durata, e data di scadenza
        if (idu != null) {
            db.getRentedMovies(idu) { rentedMovies ->
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
            }
        }
    }

    private fun isDarkTheme(): Boolean {
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

}