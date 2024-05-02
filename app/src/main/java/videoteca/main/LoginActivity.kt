package videoteca.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import videoteca.main.api.AuthService
import videoteca.main.api.DatabaseManager


class LoginActivity : AppCompatActivity() {

    private val mainScope = MainScope()
    private lateinit var sharedInfo:SharedInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        sharedInfo = SharedInfo(this)

        val tv_restore = findViewById<TextView>(R.id.tv_restorePws)
        val tv_signup = findViewById<TextView>(R.id.tv_signup)
        val btn_login = findViewById<Button>(R.id.btn_login)

        btn_login.setOnClickListener {onclickAction(it)}
        tv_restore.setOnClickListener{onclickAction(it)}
        tv_signup.setOnClickListener{onclickAction(it)}

    }

    private fun onclickAction(p0 : View?){
        if(p0 != null){
            if(p0.id == R.id.btn_login){
                val email_input = findViewById<EditText>(R.id.emailLogin_in).text.toString()
                val pws_input = findViewById<EditText>(R.id.pwsLogin_in).text.toString()
                AuthService.login(email_input, pws_input) { success, message ->
                    if (success) {
                        val db = DatabaseManager()
                        val currentUser = AuthService.getCurrentUser()
                        Log.d("UserAreaFragment", "c.user id = ${currentUser?.uid}")
                        if(currentUser?.uid != null){
                            Log.d("UserAreaFragment", "CurrentUser not null")
                            mainScope.launch {
                                val user = db.getCurrentUser(currentUser.uid)!!
                                Log.d("UserAreaFragment", "name = ${user.name}")
                                Log.d("UserAreaFragment", "surname = ${user.surname}")

                                sharedInfo.saveUserData(user.name, user.surname, currentUser.uid)
                            }
                        }
                        // Login riuscito, apri l'Activity successiva
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Optionale: chiudi l'Activity di login se non deve essere più accessibile
                    } else {
                        // Login fallito, mostra un messaggio di errore all'utente
                        Toast.makeText(this, "Login fallito: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if(p0.id == R.id.tv_restorePws){
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
            if(p0.id == R.id.tv_signup){
                val intent = Intent(this, RegistrazioneActivity::class.java)
                startActivity(intent)
            }
        }
    }




    override fun onDestroy() {
        super.onDestroy()
        // Chiudi il CoroutineScope quando l'attività viene distrutta per evitare memory leak
        mainScope.cancel()
    }




}