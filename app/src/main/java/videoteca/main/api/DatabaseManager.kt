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
import java.util.Date
import kotlin.math.abs

class DatabaseManager {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("Users")
    private val videotecaCollection = db.collection("videoteca")
    private val TAG = "DatabaseManager"

    /**
     * Aggiunge un utente nel database firebase
     * @param uid id del utente
     * @param user oggetto che raprensenta un documento Users
     * @param onComplete funzione che al completamento del caricamento resituisce l'esito del operazione
     */
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

    /**
     * resituisce un utente specifico
     * @param uid id del utente corrente
     * @return resituisce un oggetto di tipo UserDB
     */
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


    /**
     * resituisce un item specifico presente nella roccolta videoteca
     * @param idMovie richiede l'id del film (item)
     * @return resitusce un oggetto di tipo videoteca
     */
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


    /**
     * Restituisce i generi preferiti di un utente
     * @param uid id del utente
     * @return List<Int> lista dei id dei generi
     */
    fun getFavoriteGenres(uid:String, callback: (List<Int>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getFavoriteGenresAsync(uid)
            Log.d("DatabaseManager", "response : $response")
            callback(response)
        }
    }

    private suspend fun getFavoriteGenresAsync(idu: String):List<Int> {
        val userDocumentReference = usersCollection.document(idu)
        return try {
            val documentSnapshot = userDocumentReference.get().await()
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(UserDB::class.java)
                user?.favGenres ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Errore nel recupero dei generi preferiti", e)
            emptyList()
        }
    }

    /**
     * aggiunge un genere ai preferiti
     * @param idu id del utente
     * @param genreId id del genere da aggiungere
     */

    fun addFavGenre(idu: String, genreId: Int) {
        val userDocumentReference = usersCollection.document(idu)

        userDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(UserDB::class.java)
                    val favGenres = user?.favGenres?.toMutableList() ?: mutableListOf()
                    favGenres.add(genreId)

                    userDocumentReference.update("favGenres", favGenres)
                        .addOnSuccessListener {
                            Log.d("DatabaseManager", "Modifica effettuata con successo")
                        }
                        .addOnFailureListener { e ->
                            Log.e("DatabaseManager", "Errore durante l'aggiunta del genere", e)
                        }
                } else {
                    Log.e("DatabaseManager", "Il documento utente non esiste")
                }
            }
            .addOnFailureListener { e ->
                Log.e("DatabaseManager", "Errore durante il recupero del documento utente", e)
            }
    }


    /**
     * rimuove un genere preferito dalla lista dei generi preferiti
     * @param idu id del utente
     * @param idGenre id del genere da rimuovere
     */
    fun removeFavGenre(idu: String, idGenre: Int) {
        val userDocumentReference = usersCollection.document(idu)

        userDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(UserDB::class.java)
                    val favGenres = user?.favGenres?.toMutableList() ?: mutableListOf()

                    favGenres.remove(idGenre)

                    userDocumentReference.update("favGenres", favGenres)
                        .addOnSuccessListener {
                            Log.d("DatabaseManager", "genere rimosso con successo")
                        }
                        .addOnFailureListener { e ->
                            Log.e("DatabaseManager", "Errore durante la rimozione del genere", e)
                        }
                } else {
                    Log.e("DatabaseManager", "Il documento utente non esiste")
                }
            }
            .addOnFailureListener { e ->
                Log.e("DatabaseManager", "Errore durante il recupero del documento utente", e)
            }
    }



    /**
     * resituisce i film preferiti di un utente
     * @param idu l'id del utente
     * @return restituisce una lista di interi che rapresentati gli id dei film
     */
    fun getFavMovies(idu:String, callback: (List<Int>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getFavMoviesAsync(idu)
            Log.d("DatabaseManager", "response : $response")
            callback(response)
        }
    }

    /**
     * resituisce i film nella watchlist di un utente
     * @param idu l'id del utente
     * @return restituisce una lista di interi che rapresentati gli id dei film
     */
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


    /**
     * resituisce i film nollegiati di un utente
     * @param idu l'id del utente
     * @return restituisce una lista di UserDB.RentedMoviesInfo
     */
    fun getRentedMovies(idu:String, callback: (List<UserDB.RentedMoviesInfo>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRentedMoviesAsync(idu)
            Log.d("DatabaseManager", "response : $response")
            callback(response)
        }
    }

    suspend fun getRentedMoviesAsync(idu: String): List<UserDB.RentedMoviesInfo> {
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

    /**
     * aggiunge un film ai nollegiati
     * @param idu id del utente
     * @param movieId id del film da aggiungere
     */
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

    /**
     * rimuove un film dai nollegiati
     * @param idu id del utente
     * @param movieId id del film da rimuovere
     */

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


    fun removeRentedMovie(uid: String, movieIds: List<Int>, callback: (Boolean) -> Unit) {
        val userDocumentReference = usersCollection.document(uid)

        userDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(UserDB::class.java)
                    val rentedMovies = user?.rentedMovies?.toMutableList() ?: mutableListOf()

                    rentedMovies.removeAll { movie -> movieIds.contains(movie.id) }

                    userDocumentReference.update("rentedMovies", rentedMovies)
                        .addOnSuccessListener {
                            Log.d("DatabaseManager", "Film noleggiato rimosso con successo")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("DatabaseManager", "Errore durante la rimozione del film noleggiato", e)
                            callback(false)
                        }
                } else {
                    Log.e("DatabaseManager", "Il documento utente non esiste")
                    callback(false)
                }
            }
            .addOnFailureListener { e ->
                Log.e("DatabaseManager", "Errore durante il recupero del documento utente", e)
                callback(false)
            }
    }


     fun checkFilmRentedValidity(){
        val uid = AuthService.getCurrentUser()?.uid
        if (uid != null) {
            getRentedMovies(uid) { rentedMovies ->
                Log.i(TAG, "Numero di elementi noleggiati: ${rentedMovies.size}")

                val moviesToRemove = rentedMovies.filter { rented ->
                    val rentDayTimestamp = rented.rentDay?.seconds ?: 0
                    Log.i(TAG, "Film ID: ${rented.id}\nRent timestamp: $rentDayTimestamp")
                    val isExpired = checkIfMoreThanSevenDaysPassed(rentDayTimestamp)
                    Log.i(TAG, "Film scaduto: $isExpired")
                    isExpired
                }.map{it.id}

                removeRentedMovie(uid, moviesToRemove) { success ->
                    if (success) {
                        Log.i(TAG, "Film rimossi con successo")
                    } else {
                        Log.e(TAG, "Errore nella rimozione dei film")
                    }
                }

            }
        }
    }

    /**
     * controlla se dal timestamp fornito sono passati piu di 7 giorni
     * @param timestampFirebaseSeconds richiede il timestamp in secondi nel formato Long
     * @return Boolean, vero se sono passati piu di 7 giorni false altrimenti
     */
    private fun checkIfMoreThanSevenDaysPassed(timestampFirebaseSeconds: Long): Boolean {
        val firebaseDate = Date(timestampFirebaseSeconds * 1000) // Converti secondi in millisecondi
        val currentDate = Date()

        val difference = differenceInDays(firebaseDate, currentDate)
        Log.i(TAG,"diferenza di giorni: $difference")
        return difference >= 7
    }

    /**
     * calcola la differenza tra le due date
     */

    private fun differenceInDays(date1: Date, date2: Date): Long {
        val diffInMillies = abs(date2.time - date1.time)
        val diffInDays = diffInMillies / (1000 * 60 * 60 * 24)
        return diffInDays
    }




    /**
     * aggiunge un film hai preferiti
     * @param idu id del utente
     * @param movieId id del film da aggiugnere
     */
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
                    favMovies.remove(movieId)

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


                    watchlist.remove(movieId)

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