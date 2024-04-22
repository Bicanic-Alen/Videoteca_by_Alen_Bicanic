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

class GenresActivity : AppCompatActivity() {

    private val tmdbManager = TMDB_Manager()
    private val currentLocale: Locale = Locale.getDefault()
    private lateinit var recyclerViewTwoMoviesForRow:RecyclerView
    private lateinit var adapterTwoMoviesForRowAdapter: TwoMoviesForRowAdapter

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

        tmdbManager.getGenres(language){it->
            this.runOnUiThread{
                if(it!=null){
                    for (genre in it.genres){
                        if(genre.id == idGenres){
                            toolbar.title = genre.name
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