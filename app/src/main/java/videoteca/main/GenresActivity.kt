package videoteca.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import videoteca.main.Adapters.TwoMoviesForRowAdapter
import videoteca.main.api.TMDB_Manager
import java.util.Locale

/**
 * Activity che mostra elenco di film correlati a un genere specifico.
 * Mostra una lista di film in una RecyclerView, utilizzando un adapter personalizzato.
 */
class GenresActivity : AppCompatActivity() {

    private val tmdbManager = TMDB_Manager()
    private val currentLocale: Locale = Locale.getDefault()
    private lateinit var recyclerViewTwoMoviesForRow:RecyclerView
    private lateinit var adapterTwoMoviesForRowAdapter: TwoMoviesForRowAdapter

    /**
     * Metodo chiamato quando l'Activity viene creata.
     * Inizializza l'interfaccia utente, imposta la Toolbar e recupera l'ID del genere dall'intent.
     * Recupera quindi il nome del genere corrispondente e lo imposta come titolo della Toolbar.
     * Inizializza la RecyclerView per mostrare i film e recupera i film dal TMDB per il genere specificato.
     *
     * @param savedInstanceState Bundle contenente lo stato precedente dell'Activity (se presente).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_genres)

        val idGenres = intent.getIntExtra("id",0)
        val toolbar = findViewById<Toolbar>(R.id.toolbarGenres)

        val language = currentLocale.language //recupera la lingua del dispositivo (es. "it")
        val languageTag = currentLocale.toLanguageTag()

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


        //recupero dei generi
        tmdbManager.getGenres(language){it->
            this.runOnUiThread{
                if(it!=null){
                    for (genre in it.genres){
                        if(genre.id == idGenres){ //trovo una corrispondeza tra id passato al intent e l'elenco dei geniri del'api
                            toolbar.title = genre.name //imposto il nome del genere alla toolbar
                            break
                        }
                    }
                }
            }
        }

        recyclerViewTwoMoviesForRow = findViewById(R.id.recyclerView_twomoviesforrow)
        recyclerViewTwoMoviesForRow.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapterTwoMoviesForRowAdapter = TwoMoviesForRowAdapter(emptyList())
        recyclerViewTwoMoviesForRow.adapter = adapterTwoMoviesForRowAdapter

        var page:Int = 1

        //recupero dei film

        tmdbManager.getMovieDiscover(languageTag, idGenres, page){it->
            this.runOnUiThread{
                if(it!=null){
                    adapterTwoMoviesForRowAdapter = TwoMoviesForRowAdapter(it.results)
                    recyclerViewTwoMoviesForRow.adapter = adapterTwoMoviesForRowAdapter
                }
            }
        }

    }
}