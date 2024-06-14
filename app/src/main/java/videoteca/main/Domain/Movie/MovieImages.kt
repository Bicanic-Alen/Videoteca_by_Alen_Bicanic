package videoteca.main.Domain.Movie

import com.google.gson.annotations.SerializedName
/**
 * Classe di dati che rappresenta un'immagine associata a un film.
 *
 * @property aspectRatio Rapporto d'aspetto dell'immagine.
 * @property height Altezza dell'immagine in pixel.
 * @property iso Codice ISO della lingua dell'immagine.
 * @property filePath Percorso del file dell'immagine.
 * @property voteAverage Voto medio associato all'immagine.
 * @property voteCount Numero di voti ricevuti per l'immagine.
 * @property width Larghezza dell'immagine in pixel.
 * @constructor Crea un'istanza di Image con le proprietà fornite.
 */
data class Image(
    @SerializedName("aspect_ratio") val aspectRatio: Double = 0.0,
    @SerializedName("height") val height: Int = 0,
    @SerializedName("iso_639_1") val iso: String = "",
    @SerializedName("file_path") val filePath: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("width") val width: Int = 0
) {
    /**
     * Costruttore secondario per inizializzare tutte le proprietà con valori predefiniti.
     */
    // Costruttore vuoto
    constructor() : this(0.0, 0, "", "", 0.0, 0, 0)
}

/**
 * Classe di dati che rappresenta le immagini associate a un film.
 *
 * @property movieId Identificatore univoco del film.
 * @property backdrops Lista di immagini di sfondo associate al film.
 * @property logos Lista di loghi associati al film.
 * @property posters Lista di poster associati al film.
 * @constructor Crea un'istanza di MovieImages con le proprietà fornite.
 */

data class MovieImages(
    @SerializedName("id") val movieId: Int = 0,
    @SerializedName("backdrops") val backdrops: List<Image> = listOf(),
    @SerializedName("logos") val logos: List<Image> = listOf(),
    @SerializedName("posters") val posters: List<Image> = listOf()
) {
    /**
     * Costruttore secondario per inizializzare tutte le liste di immagini come liste vuote.
     */
    // Costruttore vuoto
    constructor() : this(0, listOf(), listOf(), listOf())
}
