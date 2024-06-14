package videoteca.main

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import videoteca.main.Adapters.SearchMovieAdapter
import videoteca.main.Domain.Movie.MovieResponse
import videoteca.main.api.TMDB_ImageManager
import videoteca.main.api.TMDB_Manager
import java.util.Locale


/**
 * Activity per la ricerca di film utilizzando l'API TMDB.
 * Mostra i risultati della ricerca in un RecyclerView.
 */
class SearchActivity : AppCompatActivity() {

    private val tmdbManager = TMDB_Manager()
    private val tmdbImagemanager = TMDB_ImageManager()
    private var TAG = "SearchActivity"
    private val currentLocale: Locale = Locale.getDefault()

    private lateinit var adapterSearch:SearchMovieAdapter
    private lateinit var recyclerViewSearch:RecyclerView

    /**
     * Metodo chiamato alla creazione dell'Activity.
     * Inizializza le viste e imposta la funzionalità di ricerca.
     *
     * @param savedInstanceState Bundle contenente lo stato precedente dell'Activity (se presente).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val toolbarSearch = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarSearch)
        setSupportActionBar(toolbarSearch)
        toolbarSearch.setNavigationOnClickListener {

            onBackPressed() // Per tornare all'activity precedente
        }

        val languageTag = currentLocale.toLanguageTag() //preno il tag della lingua

        adapterSearch = SearchMovieAdapter(MovieResponse().results)
        recyclerViewSearch = findViewById(R.id.recyclerView_search)
        recyclerViewSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewSearch.adapter =adapterSearch



        val tvSearch = findViewById<EditText>(R.id.etv_search_search)
        tvSearch.addTextChangedListener{ //come viene scritto un input, si aggiorna la ricerca
            tmdbManager.searchMovie(tvSearch.text.toString(), languageTag){
                this.runOnUiThread{
                    if(it!=null){
                        adapterSearch = SearchMovieAdapter(it.results)
                        recyclerViewSearch.adapter = adapterSearch
                    }
                }
            }
        }

    }
}