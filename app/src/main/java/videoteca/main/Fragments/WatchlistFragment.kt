package videoteca.main.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import videoteca.main.Adapters.TwoMoviesForRowFavAdapter
import videoteca.main.Domain.Movie.MovieDetails
import videoteca.main.R
import videoteca.main.api.AuthService
import videoteca.main.api.DatabaseManager
import videoteca.main.api.TMDB_Manager
import java.util.Locale

/**
 * Fragment che mostra la lista dei film preferiti dell'utente.
 * Utilizza un adapter personalizzato per visualizzare i dettagli dei film nella RecyclerView.
 * Gestisce il caricamento dei dati e l'interfaccia utente per la lista dei film preferiti.
 */
class WatchlistFragment : Fragment() {

    private lateinit var recyclerViewWatchlist:RecyclerView
    private lateinit var adapterWatchlist:TwoMoviesForRowFavAdapter
    private lateinit var loading:ProgressBar
    private lateinit var tvAlert:TextView

    private val tmdbApiManager = TMDB_Manager()
    private val db = DatabaseManager()
    private val utenteId = AuthService.getCurrentUser()?.uid
    private val TAG = "Whatchlist Fragment"
    private val currentLocale: Locale = Locale.getDefault()
    private val languageTag = currentLocale.toLanguageTag()


    /**
     * Metodo chiamato quando il Fragment crea la vista dell'interfaccia utente.
     * Infla il layout del fragment e inizializza i componenti della UI come RecyclerView, ProgressBar e TextView.
     * Imposta l'adapter per la RecyclerView e avvia il metodo per aggiornare la lista dei film preferiti.
     *
     * @param inflater Il layout inflater utilizzato per inflare il layout del fragment.
     * @param container Il contenitore padre in cui il fragment viene inserito.
     * @param savedInstanceState Lo stato salvato del fragment, se disponibile.
     * @return La vista radice del fragment.
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    /**
     * Metodo chiamato dopo che la vista è stata creata.
     * Configura RecyclerView e avvia il metodo per aggiornare la lista dei film preferiti.
     * Gestisce la visibilità della ProgressBar e del messaggio di avviso in base allo stato di caricamento.
     *
     * @param view La vista radice del fragment.
     * @param savedInstanceState Lo stato salvato del fragment, se disponibile.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        recyclerViewWatchlist = view.findViewById(R.id.recyclerview_watchlist)
        adapterWatchlist = TwoMoviesForRowFavAdapter(emptyList())
        recyclerViewWatchlist.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
        recyclerViewWatchlist.adapter = adapterWatchlist
        loading = view.findViewById(R.id.loading_watchlist)
        loading.visibility = View.VISIBLE
        tvAlert = view.findViewById(R.id.tv_nowatchlistitems)
        tvAlert.visibility = View.INVISIBLE
        updateFavoriteMoviesList()
    }

    /**
     * Metodo chiamato quando il Fragment torna in primo piano.
     * Aggiorna la lista dei film preferiti al ritorno al Fragment.
     * Gestisce la visibilità della ProgressBar e del messaggio di avviso in base allo stato di caricamento.
     */
    override fun onResume() {
        super.onResume()
        loading.visibility = View.VISIBLE
        tvAlert.visibility = View.INVISIBLE
        updateFavoriteMoviesList()
    }

    /*
     * Metodo che aggiorna la lista dei film preferiti dell'utente.
     * Recupera i film preferiti dall'API locale e per ogni film recupera i dettagli dal servizio TMDB.
     * Aggiorna l'adapter della RecyclerView solo quando tutti i dettagli dei film sono stati caricati.
     * Gestisce la visibilità della ProgressBar e del messaggio di avviso quando non ci sono film nella watchlist.
     */

    private fun updateFavoriteMoviesList() {
        if (utenteId != null) {
            db.getWatchlist(utenteId) { favMovie ->
                if(favMovie.isNotEmpty()){
                    Log.d(TAG, "ottengo lista watchlist: $favMovie")
                    val listWatchlistMovies: MutableList<MovieDetails> = mutableListOf()
                    for (item in favMovie) {
                        tmdbApiManager.getMovieDetails(item, languageTag) { movieDetails ->
                            if (movieDetails != null) {
                                listWatchlistMovies.add(movieDetails)
                                // Aggiorna l'adapter solo quando tutti i dettagli del film sono stati caricati
                                if (listWatchlistMovies.size == favMovie.size) {
                                    Log.d(TAG, "lista film post info dettagli: $listWatchlistMovies")
                                    activity?.runOnUiThread {
                                        tvAlert.visibility = View.GONE
                                        loading.visibility = View.GONE
                                        adapterWatchlist = TwoMoviesForRowFavAdapter(listWatchlistMovies)
                                        recyclerViewWatchlist.adapter = adapterWatchlist
                                    }
                                }
                            }
                        }
                    }
                }else{
                    activity?.runOnUiThread{
                        tvAlert.visibility = View.VISIBLE
                        loading.visibility = View.GONE
                    }
                }
            }
        }
    }


}