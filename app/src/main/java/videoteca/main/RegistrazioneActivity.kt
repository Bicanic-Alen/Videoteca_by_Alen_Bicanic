package videoteca.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import videoteca.main.api.AuthService
import videoteca.main.api.DatabaseManager
import videoteca.main.Domain.UserDB
import java.text.SimpleDateFormat
import java.util.*


/**
 * Activity per la registrazione di un nuovo utente.
 * Gestisce l'interfaccia utente per la registrazione e salva i dati dell'utente nel database.
 */
class RegistrazioneActivity : AppCompatActivity() {
    private lateinit var etvName:String
    private lateinit var etvSurname:String
    private lateinit var etvDate:String
    private lateinit var etvEmail:String
    private lateinit var etvPassword:String
    private lateinit var btnSignup: Button
    private val auth = Firebase.auth
    private val TAG = "RegistrazioneActivity"
    private lateinit var sharedInfo:SharedInfo

    /**
     * Metodo chiamato alla creazione dell'Activity.
     * Inizializza le viste e imposta i listener per i pulsanti.
     *
     * @param savedInstanceState Bundle contenente lo stato precedente dell'Activity (se presente).
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrazione)
        btnSignup = findViewById<Button>(R.id.btn_signup)

        sharedInfo = SharedInfo(this)

        btnSignup.setOnClickListener {
            Log.d(TAG,"click event btn signup")
            etvEmail = findViewById<EditText>(R.id.etv_email).text.toString()
            Log.d(TAG, "email utente $etvEmail")
            etvPassword = findViewById<EditText>(R.id.etv_password).text.toString()
            Log.d(TAG, "pws utente $etvPassword")

            AuthService.register(etvEmail, etvPassword){ success, messageError ->
                if (success){

                    etvName = findViewById<EditText>(R.id.etv_name).text.toString()
                    Log.d(TAG, "pws utente $etvName")
                    etvSurname = findViewById<EditText>(R.id.etv_surname).text.toString()
                    Log.d(TAG, "pws utente $etvSurname")
                    etvDate = findViewById<EditText>(R.id.etv_date).text.toString()
                    Log.d(TAG, "pws utente $etvDate")
                    Log.d(TAG, "createUserWithEmail:success")


                    val currentUser = AuthService.getCurrentUser()
                    val idu = currentUser?.uid
                    if(idu != null){
                        val user = UserDB(
                            name = etvName,
                            surname = etvSurname,
                            birthDate = dateToTimestamp(etvDate),
                            email = etvEmail,
                            favGenres = emptyList(),
                            favMovies = emptyList(),
                            rentedMovies = emptyList(),
                        )
                        val db = DatabaseManager()
                        db.addUser(idu, user){success, error ->
                            if(success){
                                sharedInfo.saveUserData(user.name, user.surname, idu)
                                Log.d(TAG, "add to db :success")
                                val intent = Intent(this, GenresRegActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                currentUser.delete()
                                Log.w(TAG, "add to db :failure, $error")
                                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                                val intent = Intent(this, RegistrazioneActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }


                    }
                }else{
                    Log.w(TAG, "createUserWithEmail:failure, $messageError")
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                    val intent = Intent(this, RegistrazioneActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

        }

    }


    /**
     * Converte una stringa di data in un oggetto Timestamp di Firebase.
     *
     * @param dateString La stringa della data nel formato "dd/MM/yyyy".
     * @return Un oggetto Timestamp rappresentante la data, o il timestamp corrente in caso di errore.
     */

    fun dateToTimestamp(dateString: String): Timestamp {
        // Imposta il formato desiderato della data
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        try {
            // Parsa la stringa della data in un oggetto Date
            val date = dateFormat.parse(dateString)

            // Restituisci il timestamp Firebase
            return Timestamp(date)

        } catch (e: Exception) {
            // In caso di errore nella conversione, restituisci un timestamp vuoto
            return Timestamp.now()
        }
    }

    }






