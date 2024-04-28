package videoteca.main.api

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import videoteca.main.Domain.Movie.MovieResponse
import videoteca.main.Domain.UserDB
import videoteca.main.Domain.Videoteca

class DatabaseManager {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("Users")
    private val videotecaCollection = db.collection("videoteca")

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



    fun getVidetecaItem(idMovie:Int, callback: (Videoteca?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getVideotecaItemAsync(idMovie)
            Log.d("DatabaseManager", "response : $response")
            callback(response)
        }
    }

    private suspend fun getVideotecaItemAsync(idMovie: Int):Videoteca?{
        val videotecaDocument: DocumentSnapshot
        try {
            videotecaDocument = videotecaCollection.document(idMovie.toString()).get().await()
            Log.d("DatabaseManager", "lettura avenuta con successo")
        }catch (e: Exception){
            Log.d("DatabaseManager", "return item videoteca information faileture", e)
            return null
        }

        return if(videotecaDocument.exists()){
            videotecaDocument.toObject(Videoteca::class.java)
        }else{
            null
        }
    }





    fun getFavMovies(idu:String, callback: (List<Int>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getFavMoviesAsync(idu)
            Log.d("DatabaseManager", "response : $response")
            callback(response)
        }
    }

    fun getWatchlist(idu:String, callback: (List<Int>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getWatchlistAsync(idu)
            Log.d("DatabaseManager", "response : $response")
            callback(response)
        }
    }

    private suspend fun getFavMoviesAsync(idu: String): List<Int> {
        val userDocumentReference = usersCollection.document(idu)

        return try {
            val documentSnapshot = userDocumentReference.get().await()
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(UserDB::class.java)
                user?.favMovies ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Errore nel recupero dei film preferiti", e)
            emptyList()
        }
    }


    fun getRentedMovies(idu:String, callback: (List<UserDB.RentedMoviesInfo>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRentedMoviesAsync(idu)
            Log.d("DatabaseManager", "response : $response")
            callback(response)
        }
    }

    private suspend fun getRentedMoviesAsync(idu: String): List<UserDB.RentedMoviesInfo> {
        val userDocumentReference = usersCollection.document(idu)
        return try {
            val documentSnapshot = userDocumentReference.get().await()
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(UserDB::class.java)
                user?.rentedMovies ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Errore nel recupero dei film nollegiati", e)
            emptyList()
        }
    }


    private suspend fun getWatchlistAsync(idu: String): List<Int> {
        val userDocumentReference = usersCollection.document(idu)

        return try {
            val documentSnapshot = userDocumentReference.get().await()
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(UserDB::class.java)
                user?.watchlist ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Errore nel recupero dei film nella watchlist", e)
            emptyList()
        }
    }

    fun addRentedMovies(idu: String, movieId: Int) {
        val userDocumentReference = usersCollection.document(idu)

        userDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(UserDB::class.java)
                    val rentedMovies = user?.rentedMovies?.toMutableList() ?: mutableListOf()
                    rentedMovies.add(UserDB.RentedMoviesInfo(movieId, Timestamp.now()))

                    userDocumentReference.update("rentedMovies", rentedMovies)
                        .addOnSuccessListener {
                            Log.d("DatabaseManager", "Modifica effettuata con successo")
                        }
                        .addOnFailureListener { e ->
                            Log.e("DatabaseManager", "Errore durante l'aggiunta del film noleggiato", e)
                        }
                } else {
                    Log.e("DatabaseManager", "Il documento utente non esiste")
                }
            }
            .addOnFailureListener { e ->
                Log.e("DatabaseManager", "Errore durante il recupero del documento utente", e)
            }
    }

    fun removeRentedMovie(idu: String, movieId: Int) {
        val userDocumentReference = usersCollection.document(idu)

        userDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(UserDB::class.java)
                    val rentedMovies = user?.rentedMovies?.toMutableList() ?: mutableListOf()


                    val iterator = rentedMovies.iterator()
                    while (iterator.hasNext()) {
                        val rentedMovie = iterator.next()
                        if (rentedMovie.id == movieId) {
                            iterator.remove()
                            break
                        }
                    }

                    userDocumentReference.update("rentedMovies", rentedMovies)
                        .addOnSuccessListener {
                            Log.d("DatabaseManager", "Film noleggiato rimosso con successo")
                        }
                        .addOnFailureListener { e ->
                            Log.e("DatabaseManager", "Errore durante la rimozione del film noleggiato", e)
                        }
                } else {
                    Log.e("DatabaseManager", "Il documento utente non esiste")
                }
            }
            .addOnFailureListener { e ->
                Log.e("DatabaseManager", "Errore durante il recupero del documento utente", e)
            }
    }



    fun addFavMovies(idu: String, movieId: Int) {
        val userDocumentReference = usersCollection.document(idu)

        userDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(UserDB::class.java)
                    val favMovies = user?.favMovies?.toMutableList() ?: mutableListOf()
                    favMovies.add(movieId)

                    userDocumentReference.update("favMovies", favMovies)
                        .addOnSuccessListener {
                            Log.d("DatabaseManager", "Modifica effettuata con successo")
                        }
                        .addOnFailureListener { e ->
                            Log.e("DatabaseManager", "Errore durante l'aggiunta del film preferito", e)
                        }
                } else {
                    Log.e("DatabaseManager", "Il documento utente non esiste")
                }
            }
            .addOnFailureListener { e ->
                Log.e("DatabaseManager", "Errore durante il recupero del documento utente", e)
            }
    }




    fun removeFavMovies(idu: String, movieId: Int) {
        val userDocumentReference = usersCollection.document(idu)

        userDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(UserDB::class.java)
                    val favMovies = user?.favMovies?.toMutableList() ?: mutableListOf()

                    // Rimuovi l'ID del film dalla lista
                    favMovies.remove(movieId)

                    // Aggiorna l'array nel documento
                    userDocumentReference.update("favMovies", favMovies)
                        .addOnSuccessListener {
                            Log.d("DatabaseManager", "Film preferito rimosso con successo")
                        }
                        .addOnFailureListener { e ->
                            Log.e("DatabaseManager", "Errore durante la rimozione del film preferito", e)
                        }
                } else {
                    Log.e("DatabaseManager", "Il documento utente non esiste")
                }
            }
            .addOnFailureListener { e ->
                Log.e("DatabaseManager", "Errore durante il recupero del documento utente", e)
            }
    }


    fun addToWatchlist(idu: String, movieId: Int) {
        val userDocumentReference = usersCollection.document(idu)

        userDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(UserDB::class.java)
                    val watchlist = user?.watchlist?.toMutableList() ?: mutableListOf()
                    watchlist.add(movieId)

                    userDocumentReference.update("watchlist", watchlist)
                        .addOnSuccessListener {
                            Log.d("DatabaseManager", "Modifica effettuata con successo")
                        }
                        .addOnFailureListener { e ->
                            Log.e("DatabaseManager", "Errore durante l'aggiunta del film preferito", e)
                        }
                } else {
                    Log.e("DatabaseManager", "Il documento utente non esiste")
                }
            }
            .addOnFailureListener { e ->
                Log.e("DatabaseManager", "Errore durante il recupero del documento utente", e)
            }
    }

    fun removeFromWatchlist(idu: String, movieId: Int) {
        val userDocumentReference = usersCollection.document(idu)

        userDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(UserDB::class.java)
                    val watchlist = user?.watchlist?.toMutableList() ?: mutableListOf()

                    // Rimuovi l'ID del film dalla lista
                    watchlist.remove(movieId)

                    // Aggiorna l'array nel documento
                    userDocumentReference.update("watchlist", watchlist)
                        .addOnSuccessListener {
                            Log.d("DatabaseManager", "Film preferito rimosso con successo")
                        }
                        .addOnFailureListener { e ->
                            Log.e("DatabaseManager", "Errore durante la rimozione del film preferito", e)
                        }
                } else {
                    Log.e("DatabaseManager", "Il documento utente non esiste")
                }
            }
            .addOnFailureListener { e ->
                Log.e("DatabaseManager", "Errore durante il recupero del documento utente", e)
            }
    }





}