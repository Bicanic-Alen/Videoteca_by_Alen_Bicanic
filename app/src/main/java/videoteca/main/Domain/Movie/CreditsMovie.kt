package videoteca.main.Domain.Movie

import com.google.gson.annotations.SerializedName

/**
 * Rappresenta i crediti di un film, inclusi i membri del cast e della troupe.
 *
 * @property id L'ID del film.
 * @property cast La lista dei membri del cast.
 * @property crew La lista dei membri della troupe.
 */
data class CreditsMovie(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("cast") val cast: List<Cast> = emptyList(),
    @SerializedName("crew") val crew: List<Crew> = emptyList()
) {
    /**
     * Rappresenta un membro del cast di un film.
     *
     * @property adult Indica se l'attore è adulto.
     * @property gender Il genere dell'attore (0: non specificato, 1: femmina, 2: maschio).
     * @property id L'ID dell'attore.
     * @property knownForDepartment Il dipartimento per cui è noto l'attore.
     * @property name Il nome dell'attore.
     * @property originalName Il nome originale dell'attore.
     * @property popularity La popolarità dell'attore.
     * @property profilePath Il percorso dell'immagine del profilo dell'attore.
     * @property castId L'ID del cast.
     * @property character Il nome del personaggio interpretato dall'attore.
     * @property creditId L'ID del credito dell'attore.
     * @property order L'ordine dell'attore nel cast.
     */
    data class Cast(
        @SerializedName("adult")val adult: Boolean = false,
        @SerializedName("gender")val gender: Int = 0,
        @SerializedName("id")val id: Int = 0,
        @SerializedName("known_for_department") val knownForDepartment: String = "",
        @SerializedName("name")val name: String = "",
        @SerializedName("original_name") val originalName: String = "",
        @SerializedName("popularity")val popularity: Double = 0.0,
        @SerializedName("profile_path") val profilePath: String? = null,
        @SerializedName("cast_id") val castId: Int = 0,
        @SerializedName("character") val character: String = "",
        @SerializedName("credit_id") val creditId: String = "",
        @SerializedName("order") val order: Int = 0
    )

    /**
     * Rappresenta un membro della troupe di un film.
     *
     * @property adult Indica se il membro della troupe è adulto.
     * @property gender Il genere del membro della troupe (0: non specificato, 1: femmina, 2: maschio).
     * @property id L'ID del membro della troupe.
     * @property knownForDepartment Il dipartimento per cui è noto il membro della troupe.
     * @property name Il nome del membro della troupe.
     * @property originalName Il nome originale del membro della troupe.
     * @property popularity La popolarità del membro della troupe.
     * @property profilePath Il percorso dell'immagine del profilo del membro della troupe.
     * @property creditId L'ID del credito del membro della troupe.
     * @property department Il dipartimento del membro della troupe.
     * @property job Il ruolo del membro della troupe.
     */
    data class Crew(
        @SerializedName("adult") val adult: Boolean = false,
        @SerializedName("gender") val gender: Int = 0,
        @SerializedName("id") val id: Int = 0,
        @SerializedName("known_for_department") val knownForDepartment: String = "",
        @SerializedName("name") val name: String = "",
        @SerializedName("original_name") val originalName: String = "",
        @SerializedName("popularity") val popularity: Double = 0.0,
        @SerializedName("profile_path") val profilePath: String? = null,
        @SerializedName("credit_id") val creditId: String = "",
        @SerializedName("department") val department: String = "",
        @SerializedName("job") val job: String = ""
    )
}
