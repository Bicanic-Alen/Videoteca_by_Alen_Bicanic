package videoteca.main.Domain

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName

/**
 * Rappresenta un utente nel database, con informazioni relative al nome, cognome, data di nascita,
 * email, generi preferiti, film preferiti, lista dei film da guardare e film noleggiati.
 *
 * @property name Il nome dell'utente.
 * @property surname Il cognome dell'utente.
 * @property birthDate La data di nascita dell'utente (opzionale, può essere null).
 * @property email L'indirizzo email dell'utente.
 * @property favGenres Elenco degli ID dei generi preferiti dell'utente.
 * @property favMovies Elenco degli ID dei film preferiti dell'utente.
 * @property watchlist Elenco degli ID dei film presenti nella watchlist dell'utente.
 * @property rentedMovies Elenco delle informazioni sui film noleggiati dall'utente.
 * @constructor Crea un'istanza di UserDB con valori predefiniti.
 */
data class UserDB(

    @SerializedName("name") val name: String = "",
    @SerializedName("surname") val surname: String = "",
    @SerializedName("birthDate") val birthDate: Timestamp? = null,
    @SerializedName("email") val email: String = "",
    @SerializedName("favGenres") val favGenres: List<Int> = emptyList(),
    @SerializedName("favMovies") val favMovies: List<Int> = emptyList(),
    @SerializedName("watchlist") val watchlist: List<Int> = emptyList(),
    @SerializedName("rentedMovies") val rentedMovies: List<RentedMoviesInfo> = emptyList()

) {
    /**
     * Costruttore secondario vuoto che inizializza un'istanza di UserDB con valori predefiniti.
     */
    constructor() : this("", "", null, "", emptyList(), emptyList(), emptyList(), emptyList())

    /**
     * Rappresenta le informazioni di un film noleggiato dall'utente, con l'ID del film e la data di noleggio.
     *
     * @property id L'ID del film noleggiato.
     * @property rentDay La data in cui il film è stato noleggiato (opzionale, può essere null).
     * @constructor Crea un'istanza di RentedMoviesInfo con valori predefiniti.
     */
    data class RentedMoviesInfo(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("rentDay") val rentDay:Timestamp? = null
    ){
        /**
         * Costruttore secondario vuoto che inizializza un'istanza di RentedMoviesInfo con valori predefiniti.
         */
        constructor():this(0,null)
    }

}



