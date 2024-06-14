package videoteca.main.Domain.Movie

import com.google.gson.annotations.SerializedName

/**
 * Classe di dati che rappresenta una risposta contenente informazioni sui film.
 *
 * @property page Numero della pagina della risposta.
 * @property results Lista dei film restituiti nella risposta.
 * @property totalPage Numero totale di pagine disponibili.
 * @property totalResult Numero totale di risultati disponibili.
 * @constructor Crea un'istanza di MovieResponse con le proprietà fornite.
 */
data class MovieResponse(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("results") val results: List<Movie> = emptyList(),
    @SerializedName("total_pages") val totalPage: Int = 0,
    @SerializedName("total_results") val totalResult: Int = 0
) {
    /**
     * Classe di dati che rappresenta un film.
     *
     * @property adult Indica se il film è per adulti.
     * @property backdropPath Percorso dell'immagine di sfondo del film.
     * @property genreIds Lista di ID dei generi associati al film.
     * @property id Identificatore univoco del film.
     * @property originalLanguage Lingua originale del film.
     * @property originalTitle Titolo originale del film.
     * @property overview Una breve panoramica o sinossi del film.
     * @property popularity Punteggio di popolarità del film.
     * @property posterPath Percorso dell'immagine del poster del film.
     * @property releaseDate Data di rilascio del film.
     * @property title Titolo del film.
     * @property video Indica se è disponibile un video del film.
     * @property voteAverage Voto medio del film.
     * @property voteCount Numero di voti ricevuti dal film.
     * @constructor Crea un'istanza di Movie con le proprietà fornite.
     */
    data class Movie(
        @SerializedName("adult") val adult: Boolean = false,
        @SerializedName("backdrop_path") val backdropPath: String = "",
        @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
        @SerializedName("id") val id: Int = 0,
        @SerializedName("original_language") val originalLanguage: String = "",
        @SerializedName("original_title") val originalTitle: String = "",
        @SerializedName("overview") val overview: String = "",
        @SerializedName("popularity") val popularity: Double = 0.0,
        @SerializedName("poster_path") val posterPath: String = "",
        @SerializedName("release_date") val releaseDate: String = "",
        @SerializedName("title") val title: String = "",
        @SerializedName("video") val video: Boolean = false,
        @SerializedName("vote_average") val voteAverage: Double = 0.0,
        @SerializedName("vote_count") val voteCount: Int = 0
    ) {
        /**
         * Costruttore secondario per inizializzare tutte le proprietà con valori predefiniti.
         */
        constructor() : this(
            false,
            "",
            emptyList(),
            0,
            "",
            "",
            "",
            0.0,
            "",
            "",
            "",
            false,
            0.0,
            0
        )
    }
    /**
     * Costruttore secondario per inizializzare tutte le proprietà con valori predefiniti.
     */
    constructor() : this(
        0,
        emptyList(),
        0,
        0
    )
}


