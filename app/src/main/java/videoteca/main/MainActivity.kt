   package videoteca.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import videoteca.main.Fragments.FavFragment
import videoteca.main.Fragments.HomeFragment
import videoteca.main.Fragments.UserAreaFragment
import videoteca.main.Fragments.WatchlistFragment
import videoteca.main.api.AuthService
import videoteca.main.api.DatabaseManager
import videoteca.main.api.TMDB_Manager
import java.util.Date
import java.util.Locale
import kotlin.math.abs

   class MainActivity : AppCompatActivity() {

       private val db = DatabaseManager()

       /**
        * imposta la vista dell' l'activity e inizializzazione delle componenti
        *
        * @param savedInstanceState Bundle contenente lo stato precedente dell'Activity (se presente).
        */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main) //imposto il layout dell' activity
        checkCurrentUser() //controllo che l'utente è autenticato

        //configuro la toolbar, imposto un listener per gli elementi del menu
        val toolbarMain = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMain)
        toolbarMain.setOnMenuItemClickListener{ topMenuItem ->
            when(topMenuItem.itemId){
                R.id.search ->{ // se l'elemento è search allora passo alla activity di ricerca
                    val intent = Intent(this, SearchActivity::class.java)
                    this.startActivity(intent)
                    true
                }
                else -> false
            }



        }

        db.checkFilmRentedValidity() //controllo se ci sono film scaduti nel noleggio dei film, e li rimuovo

        //imposto il colore della status  bar in base al tema usato
        val statusBarColor = if (isDarkTheme()) {
            ContextCompat.getColor(this, R.color.black)
        } else {
            ContextCompat.getColor(this, R.color.gray_300)
        }

        window.statusBarColor = statusBarColor

       // Impostazione del colore della NavigationBar
       val navigationBarColor = if (isDarkTheme()) {
           ContextCompat.getColor(this, R.color.black)
       } else {
           ContextCompat.getColor(this, R.color.gray_300)
       }
       window.navigationBarColor = navigationBarColor



        // configurazione  della navbar per la navigazione tra i diversi fragment,
        // e modifico il titolo della toolbar in base alla "pagina" selezionata

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.home -> {
                    toolbarMain.title = getString(R.string.app_name)
                    toolbarMain.menu.setGroupVisible(0,true)
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.favorites -> {
                    toolbarMain.title = getString(R.string.favorites)
                    toolbarMain.menu.setGroupVisible(0,false)
                    replaceFragment(FavFragment())
                    true

                }

                R.id.watchlist -> {
                    toolbarMain.title = getString(R.string.watchlist)
                    toolbarMain.menu.setGroupVisible(0,false)
                    replaceFragment(WatchlistFragment())
                    true
                }

                R.id.user_area ->{
                    toolbarMain.title = getString(R.string.user_area)
                    toolbarMain.menu.setGroupVisible(0,false)
                    replaceFragment(UserAreaFragment())
                    true
                }
                else -> false

            }



        }

        replaceFragment(HomeFragment())

    }

   /**
    * Controlla se l'utente è autenticato. Se non lo è, avvia l'attività di login.
    */
    private fun checkCurrentUser() {
        // [START check_current_user]
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
        } else {
            startLoginActivity()
        }

    }

   /**
    * Avvia LoginActivity e chiude l'attività corrente.
    */
    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() //chiudo la activity corrente
    }

   /**
    * Sostituisce il fragment attuale con uno nuovo.
    * @param fragment Fragment rapresenta il framento con il quale sostituire l'attuale visualizzato.
    */
   private fun replaceFragment(fragment: Fragment) {
       supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()

   }


   /**
    * Controlla se il tema attuale è scuro.
    * @return restituisce un booleano (true = night mode, false = light mode)
    */

   private fun isDarkTheme(): Boolean {
       //accesso alla configurazione corrente delle risorse, uiMode mi fornisce informazioni sulla modalità dell'interfaccia utente;
       // e vado a vedere tramite la maschera che mi filtra la modalità nottura se il dispositivo è modalità nottura
       val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
       //restituisco true se mode è in night mode
       return mode == Configuration.UI_MODE_NIGHT_YES
   }
}