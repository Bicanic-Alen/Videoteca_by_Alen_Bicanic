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


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.favorites -> {
                    replaceFragment(FavFragment())
                    true
                }

                R.id.watchlist -> {
                    replaceFragment(WatchlistFragment())
                    true
                }

                R.id.user_area ->{
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