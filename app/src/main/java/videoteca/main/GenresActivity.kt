package videoteca.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import videoteca.main.gestioneAPI.TMDB_Manager
import java.util.Locale

class GenresActivity : AppCompatActivity() {

    private val tmdbManager = TMDB_Manager()
    private val currentLocale: Locale = Locale.getDefault()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_genres)

        val idGenres = intent.getIntExtra("id",0)
        val toolbar = findViewById<Toolbar>(R.id.toolbarGenres)

        val language = currentLocale.language //recupera la lingua del dispositivo (es. "it")
        val languageTag = currentLocale.toLanguageTag()

        val toolbarDetails = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarGenres)
        setSupportActionBar(toolbarDetails)
        toolbarDetails.setNavigationOnClickListener {
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
    }
}