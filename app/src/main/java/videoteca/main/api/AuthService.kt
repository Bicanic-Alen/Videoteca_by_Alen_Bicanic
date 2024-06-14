package videoteca.main.api

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Oggetto che gestisce l'autenticazione degli utenti utilizzando Firebase Authentication.
 */
object AuthService {

    private val mAuth = FirebaseAuth.getInstance()
    private val TAG = "AuthService"




    /**
     * Registra un nuovo utente con email e password.
     *
     * @param email L'email dell'utente da registrare.
     * @param password La password dell'utente da registrare.
     * @param onComplete Callback chiamato al termine dell'operazione di registrazione.
     *                   Restituisce un booleano che indica il successo o il fallimento e un messaggio di errore opzionale.
     */
    fun register(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        Log.d(TAG, "register() called with email: $email")
        Log.d(TAG, "register() called with pws: $password")
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Registrazione riuscita
                } else {
                    onComplete(false, task.exception?.message) // Registrazione fallita
                }
            }
    }




    /**
     * Effettua il login con email e password.
     *
     * @param email L'email dell'utente che vuole effettuare il login.
     * @param password La password dell'utente che vuole effettuare il login.
     * @param onComplete Callback chiamato al termine dell'operazione di login.
     *                   Restituisce un booleano che indica il successo o il fallimento e un messaggio di errore opzionale.
     */
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

    /**
     * Ottiene l'utente attualmente autenticato.
     *
     * @return L'utente corrente autenticato o null se nessun utente è autenticato.
     */
    fun getCurrentUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    /**
     * Effettua il logout dell'utente corrente.
     */
    fun logout() {
        mAuth.signOut()
    }



}