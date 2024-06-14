package videoteca.main.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import videoteca.main.Adapters.SearchMovieAdapter
import videoteca.main.Adapters.TwoMoviesForRowFavAdapter
import videoteca.main.Domain.Movie.MovieDetails
import videoteca.main.R
import videoteca.main.api.AuthService
import videoteca.main.api.DatabaseManager
import videoteca.main.api.TMDB_Manager
import java.util.Locale

/**
 * Fragment che gestisce la visualizzazione dei film preferiti dell'utente.
 */

class FavFragment : Fragment() {

    private lateinit var adapterFavorite: TwoMoviesForRowFavAdapter
    private val tmdbApiManager = TMDB_Manager()
    private var TAG = "FavFregment"
    private val currentLocale: Locale = Locale.getDefault()
    private val languageTag = currentLocale.toLanguageTag()
    private val db = DatabaseManager()
    private lateinit var recyclerViewFavMovies:RecyclerView
    private lateinit var loading:ProgressBar
    private lateinit var tvAlert: TextView
    private val utenteId = AuthService.getCurrentUser()?.uid

    /**
     * Metodo chiamato per creare la vista del fragment.
     * Infla il layout XML associato a questo fragment.
     *
     * @param inflater Il LayoutInflater che può essere utilizzato per inflare qualsiasi layout XML.
     * @param container Il contenitore padre in cui viene inserito il fragment.
     * @param savedInstanceState Dati dell'istanza precedente del fragment salvati.
     * @return La vista radice del fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav, container, false)
    }

    /**
     * Metodo chiamato subito dopo che onCreateView() è stato chiamato per la prima volta.
     * Inizializza le visualizzazioni e aggiorna la lista dei film preferiti.
     *
     * @param view La vista creata in onCreateView().
     * @param savedInstanceState Dati dell'istanza precedente del fragment salvati.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        recyclerViewFavMovies = view.findViewById(R.id.recyclerView_favoritemovie)
        adapterFavorite = TwoMoviesForRowFavAdapter(emptyList())
        recyclerViewFavMovies.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
        recyclerViewFavMovies.adapter = adapterFavorite
        loading = view.findViewById(R.id.progressBar_fav)
        loading.visibility = View.VISIBLE
        tvAlert = view.findViewById(R.id.tv_nofav)
        tvAlert.visibility = View.INVISIBLE
        updateFavoriteMoviesList()
    }

    /**
     * Metodo chiamato quando il fragment torna in primo piano dopo essere stato in pausa.
     * Aggiorna la lista dei film preferiti.
     */
    override fun onResume() {
        super.onResume()
        loading.visibility = View.VISIBLE
        tvAlert.visibility = View.INVISIBLE
        updateFavoriteMoviesList()
    }

    /*
     * Metodo per aggiornare la lista dei film preferiti dell'utente.
     * Ottiene i dettagli dei film preferiti dall'API TMDB e aggiorna l'adapter della RecyclerView.
     * Mostra un messaggio se non ci sono film preferiti o se si verifica un errore nel recupero dei dati.
     */
    private fun updateFavoriteMoviesList() {
        if (utenteId != null) {
            db.getFavMovies(utenteId) { favMovie ->
                if(favMovie.isNotEmpty()) {
                    Log.d(TAG, "ottengo lista favoriti: ${favMovie.toString()}")
                    val listFavMovies: MutableList<MovieDetails> = mutableListOf()
                    for (item in favMovie) {
                        tmdbApiManager.getMovieDetails(item, languageTag) { movieDetails ->
                            if (movieDetails != null) {
                                Log.d(TAG, "aggiungo film alla lista: ${movieDetails.title}")
                                listFavMovies.add(movieDetails)
                                // Aggiorna l'adapter solo quando tutti i dettagli del film sono stati caricati
                                if (listFavMovies.size == favMovie.size) {
                                    activity?.runOnUiThread {
                                        tvAlert.visibility = View.GONE
                                        loading.visibility = View.GONE
                                        adapterFavorite = TwoMoviesForRowFavAdapter(listFavMovies)
                                        recyclerViewFavMovies.adapter = adapterFavorite
                                    }
                                }
                            }
                        }
                    }
                }else{
                    activity?.runOnUiThread{
                        loading.visibility = View.GONE
                        tvAlert.visibility = View.VISIBLE
                    }
                }
            }
        }
    }



}