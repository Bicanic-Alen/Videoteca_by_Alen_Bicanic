package videoteca.main.Domain

import com.google.gson.annotations.SerializedName

/**
 * Classe che rappresenta una lista di generi di film.
 *
 * @property genres Lista dei generi di film.
 * @constructor Crea un'istanza di GenreList con la lista di generi specificata.
 */
data class GenreList(
    @SerializedName("genres") val genres: List<Genre>
) {
    /**
     * Data class che rappresenta un singolo genere di film.
     *
     * @property id ID del genere.
     * @property name Nome del genere.
     * @constructor Crea un'istanza di Genre con l'ID e il nome del genere specificati.
     */
    data class Genre(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
    )

    /**
     * Costruttore secondario che inizializza la lista di generi con una lista vuota.
     */
    constructor() : this(emptyList()) // Costruttore vuoto che inizializza la lista di generi come lista vuota
}



