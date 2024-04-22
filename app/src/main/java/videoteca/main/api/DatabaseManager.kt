package videoteca.main.api

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import videoteca.main.Domain.UserDB

class DatabaseManager {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("Users")

    fun addUser(uid: String, user: UserDB, onComplete: (Boolean, String?) -> Unit) {

        Log.d("DatabaseManager", "uid: $uid")
        Log.d("DatabaseManager", "User: " +
                "[nome = ${user.name}, " +
                "cognome = ${user.surname}," +
                "data = ${user.birthDate}" +
                "email = ${user.email}")

        usersCollection.document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("DatabaseManager", "add to database success")
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                Log.d("DatabaseManager", "add to database faileture", e)
                onComplete(false, e.message)
            }
    }

    suspend fun getCurrentUser(uid: String): UserDB? {
        val userDocument: DocumentSnapshot //visualizzo un istante del documento
        try {
            userDocument = usersCollection.document(uid).get().await()
            Log.d("DatabaseManager", "lettura avenuta con successo")
        } catch (e: Exception) {
            Log.d("DatabaseManager", "return user information faileture", e)
            return null
        }

        return if (userDocument.exists()) {
            userDocument.toObject(UserDB::class.java)
        } else {
            null
        }
    }


}