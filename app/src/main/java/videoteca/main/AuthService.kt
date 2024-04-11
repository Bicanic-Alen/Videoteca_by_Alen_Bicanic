package videoteca.main

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


object AuthService {

    private val mAuth = FirebaseAuth.getInstance()



    // Metodo per registrare un nuovo utente con email e password
    fun register(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Registrazione riuscita
                } else {
                    onComplete(false, task.exception?.message) // Registrazione fallita
                }
            }
    }

    // Metodo per effettuare il login con email e password
    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Login riuscito
                } else {
                    onComplete(false, task.exception?.message) // Login fallito
                }
            }
    }

    // Metodo per ottenere l'utente corrente
    fun getCurrentUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    // Metodo per effettuare il logout
    fun logout() {
        mAuth.signOut()
    }



}