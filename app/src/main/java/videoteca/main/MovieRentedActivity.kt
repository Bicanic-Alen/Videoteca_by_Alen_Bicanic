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

/**
 * Activity per visualizzare i film noleggiati dall'utente.
 * Mostra una lista di film noleggiati con i dettagli come titolo, copertina e data di scadenza del noleggio.
 */
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

    /**
     * Metodo viene chiamato alla creazione dell'Activity.
     * Inizializza le componenti e carica i dati dei film noleggiati.
     *
     * @param savedInstanceState Bundle contenente lo stato precedente dell'Activity (se presente).
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_rented)

        //setto il colore della status bar
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

        //inizializzo l'adapter
        adapterRentedMovies = RentedMovieAdapter(emptyList())
        recyclerViewRentedMovies.adapter = adapterRentedMovies


        db.checkFilmRentedValidity() //verifico la validità dei titoli

        Log.d(TAG, "è gia stato inzializato: $flagInit")
        if(!flagInit) {
            Log.d(TAG, "sono nel if check flag")
            initInfo()
        }

    }




    /**
     * Restituisce la data di quando scade un titolo
     * @param timestampFirebaseSeconds Timestamp in secondi dal quale calcolare la data di scadenza.
     * @return La data di scadenza del noleggio.
     */
    private fun getExpirationDate(timestampFirebaseSeconds: Long): Date {
        val rentDate = Date(timestampFirebaseSeconds * 1000) // secondi in millisecondi
        val calendar = Calendar.getInstance()
        calendar.time = rentDate
        calendar.add(Calendar.DAY_OF_YEAR, 7) // + 7 giorni
        return calendar.time
    }

    /**
     * Inizializza le informazioni sui titoli noleggiati.
     * Recupera i dettagli dei film noleggiati dall'utente e li visualizza nel RecyclerView.
     */
    private fun initInfo(){
        flagInit = true
        //ottengo informazioni per la copertina e titolo e durata, e data di scadenza
        if (uid != null) {
            db.getRentedMovies(uid) { rentedMovies ->
                if(rentedMovies.isNotEmpty()){
                    runOnUiThread{ //eseguo sul thread principale, perche non posso modificare le componenti grafici in thread secondari
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
                                if(idMovie == 0) { //controllo se l'id non è valido
                                    contIdNull++ //conto il numeri di id non validi
                                }
                                else{ //se valido recupero le inforazioni e la aggiungo alla lista dei film da visuallizzare
                                    val rentedMovieInfo = MovieRentedInfo(idMovie, expirationDate, movieDetails.posterPath, movieDetails.title, movieDetails.releaseDate)
                                    listRent.add(rentedMovieInfo)
                                }

                                if (listRent.size == rentedMovies.size-contIdNull) { //verifico di aver recuperato tutte le informazioni
                                    runOnUiThread {
                                        loading.visibility = View.GONE //rimuovo il loading
                                        adapterRentedMovies = RentedMovieAdapter(listRent) //carico nel adapter la lista dei film da visualizzare
                                        recyclerViewRentedMovies.adapter = adapterRentedMovies // inserisco nella recyclerView l'elenco dei film da visualizzare
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

    /**
     * Verifica se il tema attuale è scuro.
     *
     * @return True se il tema è scuro, False altrimenti.
     */
    private fun isDarkTheme(): Boolean {
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

}