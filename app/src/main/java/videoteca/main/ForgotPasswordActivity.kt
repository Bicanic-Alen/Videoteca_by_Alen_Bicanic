package videoteca.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import videoteca.main.api.AuthService

/**
 * Activity per la gestione del recupero della password.
 */
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var btn_recover:Button
    private lateinit var btn_back:Button
    private lateinit var et_email: EditText

    /**
     * Metodo chiamato alla creazione dell'activity.
     *
     * @param savedInstanceState Stato precedente dell'istanza, se disponibile.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password2)

        btn_recover = findViewById(R.id.btn_recover_pws)
        btn_back = findViewById(R.id.btn_back)
        et_email = findViewById(R.id.et_email_forgot)

        btn_recover.setOnClickListener{
            val email:String = et_email.text.toString().trim()
            if(email.isEmpty()){
                Toast.makeText(this,
                    getString(R.string.no_email_has_been_entered), Toast.LENGTH_SHORT).show()
            }else{
                resetPassword(email)
            }
        }

    }

    /**
     * Avvia il processo di reset della password utilizzando il servizio di autenticazione.
     *
     * @param email L'indirizzo email per il quale eseguire il reset della password.
     */
    private fun resetPassword(email:String) {
        AuthService.resetPassword(this, email){ success, message ->
            if(success){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}