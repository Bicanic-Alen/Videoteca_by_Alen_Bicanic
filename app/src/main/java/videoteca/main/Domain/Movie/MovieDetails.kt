package videoteca.main.Domain.Movie

import com.google.gson.annotations.SerializedName

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
    constructor() : this(false)
}

data class Collection(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("poster_path") val posterPath: String = "",
    @SerializedName("backdrop_path") val backdropPath: String = ""
) {
    constructor() : this(0)
}

data class Genre(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = ""
) {
    constructor() : this(0)
}

data class ProductionCompany(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("logo_path") val logoPath: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("origin_country") val originCountry: String = ""
) {
    constructor() : this(0)
}

data class ProductionCountry(
    @SerializedName("iso_3166_1") val iso31661: String = "",
    @SerializedName("name") val name: String = ""
) {
    constructor() : this("")
}

data class SpokenLanguage(
    @SerializedName("english_name") val englishName: String = "",
    @SerializedName("iso_639_1") val iso6391: String = "",
    @SerializedName("name") val name: String = ""
) {
    constructor() : this("")
}

data class Videos(
    @SerializedName("results") val results: List<VideoResult> = emptyList()
)

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
