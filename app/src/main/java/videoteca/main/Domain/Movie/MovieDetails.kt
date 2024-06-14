package videoteca.main.Domain.Movie

import com.google.gson.annotations.SerializedName

/**
 * Classe di dati che rappresenta i dettagli di un film.
 *
 * @property adult Indica se il film è per adulti.
 * @property backdropPath Percorso dell'immagine di sfondo del film.
 * @property belongsToCollection Informazioni sulla collezione a cui il film appartiene.
 * @property budget Budget del film.
 * @property genres Lista dei generi del film.
 * @property homepage URL della homepage del film.
 * @property id Identificatore univoco del film.
 * @property imdbId Identificatore IMDb del film.
 * @property originCountry Lista dei paesi di origine del film.
 * @property originalLanguage Lingua originale del film.
 * @property originalTitle Titolo originale del film.
 * @property overview Sinossi del film.
 * @property popularity Popolarità del film.
 * @property posterPath Percorso del poster del film.
 * @property productionCompanies Lista delle compagnie di produzione del film.
 * @property productionCountries Lista dei paesi di produzione del film.
 * @property releaseDate Data di uscita del film.
 * @property revenue Ricavo totale del film.
 * @property runtime Durata del film in minuti.
 * @property spokenLanguages Lista delle lingue parlate nel film.
 * @property status Stato di produzione del film.
 * @property tagline Slogan del film.
 * @property title Titolo del film.
 * @property video Indica se il film ha un video associato.
 * @property voteAverage Voto medio del film.
 * @property voteCount Numero di voti totali ricevuti dal film.
 * @property videos Informazioni sui video associati al film.
 * @constructor Crea un'istanza di MovieDetails con le proprietà fornite.
 */
data class MovieDetails(
    @SerializedName("adult") val adult: Boolean = false,
    @SerializedName("backdrop_path") val backdropPath: String = "",
    @SerializedName("belongs_to_collection") val belongsToCollection: Collection? = null,
    @SerializedName("budget") val budget: Int = 0,
    @SerializedName("genres") val genres: List<Genre> = emptyList(),
    @SerializedName("homepage") val homepage: String = "",
    @SerializedName("id") val id: Int = 0,
    @SerializedName("imdb_id") val imdbId: String = "",
    @SerializedName("origin_country") val originCountry: List<String> = emptyList(),
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("original_title") val originalTitle: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String = "",
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany> = emptyList(),
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry> = emptyList(),
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("revenue") val revenue: Long = 0,
    @SerializedName("runtime") val runtime: Int = 0,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage> = emptyList(),
    @SerializedName("status") val status: String = "",
    @SerializedName("tagline") val tagline: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("video") val video: Boolean = false,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("videos") val videos: Videos? = null
) {
    /**
     * Costruttore secondario per inizializzare solo il campo "adult" del film.
     */
    constructor() : this(false)
}

/**
 * Classe di dati che rappresenta una collezione a cui un film appartiene.
 *
 * @property id Identificatore univoco della collezione.
 * @property name Nome della collezione.
 * @property posterPath Percorso del poster della collezione.
 * @property backdropPath Percorso dell'immagine di sfondo della collezione.
 * @constructor Crea un'istanza di Collection con le proprietà fornite.
 */
data class Collection(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("poster_path") val posterPath: String = "",
    @SerializedName("backdrop_path") val backdropPath: String = ""
) {
    /**
     * Costruttore secondario per inizializzare tutte le proprietà con valori predefiniti.
     */
    constructor() : this(0)
}

/**
 * Classe di dati che rappresenta un genere associato a un film.
 *
 * @property id Identificatore univoco del genere.
 * @property name Nome del genere.
 * @constructor Crea un'istanza di Genre con le proprietà fornite.
 */
data class Genre(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = ""
) {
    /**
     * Costruttore secondario per inizializzare tutte le proprietà con valori predefiniti.
     */
    constructor() : this(0)
}

/**
 * Classe di dati che rappresenta una compagnia di produzione associata a un film.
 *
 * @property id Identificatore univoco della compagnia di produzione.
 * @property logoPath Percorso del logo della compagnia di produzione.
 * @property name Nome della compagnia di produzione.
 * @property originCountry Paese di origine della compagnia di produzione.
 * @constructor Crea un'istanza di ProductionCompany con le proprietà fornite.
 */
data class ProductionCompany(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("logo_path") val logoPath: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("origin_country") val originCountry: String = ""
) {
    /**
     * Costruttore secondario per inizializzare tutte le proprietà con valori predefiniti.
     */
    constructor() : this(0)
}
/**
 * Classe di dati che rappresenta un paese di produzione associato a un film.
 *
 * @property iso31661 Codice ISO del paese.
 * @property name Nome del paese di produzione.
 * @constructor Crea un'istanza di ProductionCountry con le proprietà fornite.
 */
data class ProductionCountry(
    @SerializedName("iso_3166_1") val iso31661: String = "",
    @SerializedName("name") val name: String = ""
) {
    /**
     * Costruttore secondario per inizializzare il codice ISO con una stringa vuota.
     */
    constructor() : this("")
}

/**
 * Classe di dati che rappresenta una lingua parlata in un film.
 *
 * @property englishName Nome in lingua inglese della lingua.
 * @property iso6391 Codice ISO-639-1 della lingua.
 * @property name Nome della lingua.
 * @constructor Crea un'istanza di SpokenLanguage con le proprietà fornite.
 */
data class SpokenLanguage(
    @SerializedName("english_name") val englishName: String = "",
    @SerializedName("iso_639_1") val iso6391: String = "",
    @SerializedName("name") val name: String = ""
) {
    /**
     * Costruttore secondario per inizializzare tutte le proprietà con stringhe vuote.
     */
    constructor() : this("")
}

/**
 * Classe di dati che rappresenta i video associati a un film.
 *
 * @property results Lista dei risultati dei video associati al film.
 * @constructor Crea un'istanza di Videos con i risultati forniti.
 */
data class Videos(
    @SerializedName("results") val results: List<VideoResult> = emptyList()
)

/**
 * Classe di dati che rappresenta il risultato di un video associato a un film.
 *
 * @property iso6391 Codice ISO-639-1 della lingua del video.
 * @property iso31661 Codice ISO-3166-1 del paese del video.
 * @property name Nome del video.
 * @property key Chiave del video utilizzata per accedere al video.
 * @property site Sito web su cui è disponibile il video.
 * @property size Dimensione del video.
 * @property type Tipo del video.
 * @property official Indica se il video è ufficiale.
 * @property publishedAt Data di pubblicazione del video.
 * @property id Identificatore univoco del video.
 * @constructor Crea un'istanza di VideoResult con le proprietà fornite.
 */
data class VideoResult(
    @SerializedName("iso_639_1") val iso6391: String = "",
    @SerializedName("iso_3166_1") val iso31661: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("key") val key: String = "",
    @SerializedName("site") val site: String = "",
    @SerializedName("size") val size: Int = 0,
    @SerializedName("type") val type: String = "",
    @SerializedName("official") val official: Boolean = false,
    @SerializedName("published_at") val publishedAt: String = "",
    @SerializedName("id") val id: String = ""
)
