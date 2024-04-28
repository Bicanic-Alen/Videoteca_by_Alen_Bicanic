   package videoteca.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import videoteca.main.Fragments.FavFragment
import videoteca.main.Fragments.HomeFragment
import videoteca.main.Fragments.UserAreaFragment
import videoteca.main.Fragments.WatchlistFragment

   class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        checkCurrentUser()

        val toolbarMain = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMain)
        toolbarMain.setOnMenuItemClickListener{ topMenuItem ->
            when(topMenuItem.itemId){
                R.id.search ->{
                    val intent = Intent(this, SearchActivity::class.java)
                    this.startActivity(intent)
                    true
                }
                else -> false
            }

        }


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


    private fun checkCurrentUser() {
        // [START check_current_user]
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
        } else {
            startLoginActivity()
        }

    }


    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() //chiudo la activity corrente
    }

   private fun replaceFragment(fragment: Fragment){
       supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()

   }
}