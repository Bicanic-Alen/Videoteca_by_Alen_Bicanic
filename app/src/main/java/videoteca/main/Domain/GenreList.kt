package videoteca.main.Domain

import com.google.gson.annotations.SerializedName

data class GenreList(
    @SerializedName("genres") val genres: List<Genre>
) {
    data class Genre(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
    )

    constructor() : this(emptyList()) // Costruttore vuoto che inizializza la lista di generi come lista vuota
}



