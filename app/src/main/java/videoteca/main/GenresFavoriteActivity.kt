package videoteca.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import videoteca.main.Adapters.FavoriteGenreAdapter
import videoteca.main.Domain.GenreList
import videoteca.main.api.DatabaseManager
import videoteca.main.api.TMDB_Manager
import java.util.Locale

/**
 * Activity che mostra i generi cinematografici preferiti dell'utente.
 * Mostra una lista di generi preferiti in una RecyclerView.
 */

class GenresFavoriteActivity : AppCompatActivity() {

    private lateinit var favAdapter: FavoriteGenreAdapter
    private lateinit var recyclerView: RecyclerView

    private val db = DatabaseManager()
    private val tmdbManager = TMDB_Manager()
    private var TAG = "GenresFavoriteActivity"
    private val currentLocale: Locale = Locale.getDefault()

    /**
     * Metodo chiamato quando l'Activity viene creata.
     * Inizializza l'interfaccia utente e recupera i generi dal TMDB per popolare l'adapter della RecyclerView.
     *
     * @param savedInstanceState Bundle contenente lo stato precedente dell'Activity (se presente).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_genres_favorite)

        val statusBarColor = if (isDarkTheme()) {
            ContextCompat.getColor(this, R.color.black)
        } else {
            ContextCompat.getColor(this, R.color.gray_300)
        }

        window.statusBarColor = statusBarColor

        val toolbar = findViewById<Toolbar>(R.id.favgen_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        favAdapter = FavoriteGenreAdapter(GenreList())
        recyclerView = findViewById(R.id.recyclerview_favgenres)
        recyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)

        recyclerView.adapter = favAdapter



        tmdbManager.getGenres(currentLocale.language){genreList ->
            this.runOnUiThread{
                if(genreList!=null){
                    favAdapter = FavoriteGenreAdapter(genreList)
                    recyclerView.adapter = favAdapter
                }
            }
        }


    }


    /**
     * Metodo chiamato quando l'Activity viene creata.
     * Inizializza l'interfaccia utente e recupera i generi dal TMDB per popolare l'adapter della RecyclerView.
     *
     * @param savedInstanceState Bundle contenente lo stato precedente dell'Activity (se presente).
     */
    private fun isDarkTheme(): Boolean {
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }
}