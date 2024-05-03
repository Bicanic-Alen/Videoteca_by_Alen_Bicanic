package videoteca.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import videoteca.main.Adapters.FavoriteGenreAdapter
import videoteca.main.Domain.GenreList
import videoteca.main.api.DatabaseManager
import videoteca.main.api.TMDB_Manager
import java.util.Locale

class GenresRegActivity : AppCompatActivity() {
    private lateinit var favAdapter: FavoriteGenreAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnContinue: Button

    private val db = DatabaseManager()
    private val tmdbManager = TMDB_Manager()
    private var TAG = "GenresRegFavoriteActivity"
    private val currentLocale: Locale = Locale.getDefault()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_genres_reg)
        val statusBarColor = if (isDarkTheme()) {
            ContextCompat.getColor(this, R.color.black)
        } else {
            ContextCompat.getColor(this, R.color.gray_300)
        }

        window.statusBarColor = statusBarColor

        favAdapter = FavoriteGenreAdapter(GenreList())
        recyclerView = findViewById(R.id.recyclerview_genres_reg_fav)
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

        btnContinue = findViewById(R.id.btn_continue)
        btnContinue.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }




    }

    private fun isDarkTheme(): Boolean {
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }
}