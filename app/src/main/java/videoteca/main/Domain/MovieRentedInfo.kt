package videoteca.main.Domain

import java.util.Date

/**
 * Classe che rappresenta le informazioni di un film affittato.
 *
 * @property id ID del film affittato.
 * @property dateExpiration Data di scadenza dell'affitto.
 * @property posterPath Percorso del poster del film.
 * @property title Titolo del film.
 * @property dateMovie Data di uscita del film.
 * @constructor Crea un'istanza di MovieRentedInfo con valori predefiniti o nulli.
 */
data class MovieRentedInfo(
    val id: Int = 0,
    val dateExpiration: Date? = null,
    val posterPath:String = "",
    val title:String = "",
    val dateMovie: String = ""
    ){

    /**
     * Costruttore secondario che inizializza tutti i campi con valori predefiniti o nulli.
     */
    constructor():this(0,null,"", "", "")
}
