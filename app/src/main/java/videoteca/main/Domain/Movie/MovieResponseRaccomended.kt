package videoteca.main.Domain.Movie

import com.google.gson.annotations.SerializedName


/**
 * Classe di dati che rappresenta una risposta contenente film consigliati.
 *
 * @property page Numero della pagina della risposta.
 * @property results Lista dei film consigliati.
 * @property totalPages Numero totale di pagine disponibili.
 * @property totalResults Numero totale di risultati disponibili.
 * @constructor Crea un'istanza di MovieResponseRecommended con le proprietà fornite.
 */
data class MovieResponseRecommended(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieRaccomended>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
) {
    /**
     * Classe di dati che rappresenta un film consigliato.
     *
     * @property backdropPath Percorso dell'immagine di sfondo del film.
     * @property id Identificatore univoco del film.
     * @property originalTitle Titolo originale del film.
     * @property overview Una breve panoramica o sinossi del film.
     * @property posterPath Percorso dell'immagine del poster del film.
     * @property mediaType Il tipo di media (ad esempio, film).
     * @property adult Indica se il film è adatto ai minori.
     * @property title Il titolo del film.
     * @property originalLanguage La lingua originale del film.
     * @property genreIds Lista di ID dei generi associati al film.
     * @property popularity Il punteggio di popolarità del film.
     * @property releaseDate La data di uscita del film.
     * @property video Indica se è disponibile un video del film.
     * @property voteAverage Il voto medio del film.
     * @property voteCount Il numero di voti ricevuti dal film.
     * @constructor Crea un'istanza di MovieRaccomended con le proprietà fornite.
     */
    data class MovieRaccomended(
        @SerializedName("backdrop_path")
        val backdropPath: String?,
        @SerializedName("id")
        val id: Int,
        @SerializedName("original_title")
        val originalTitle: String,
        @SerializedName("overview")
        val overview: String,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("media_type")
        val mediaType: String,
        @SerializedName("adult")
        val adult: Boolean,
        @SerializedName("title")
        val title: String,
        @SerializedName("original_language")
        val originalLanguage: String,
        @SerializedName("genre_ids")
        val genreIds: List<Int>,
        @SerializedName("popularity")
        val popularity: Double,
        @SerializedName("release_date")
        val releaseDate: String,
        @SerializedName("video")
        val video: Boolean,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int
    )
}
