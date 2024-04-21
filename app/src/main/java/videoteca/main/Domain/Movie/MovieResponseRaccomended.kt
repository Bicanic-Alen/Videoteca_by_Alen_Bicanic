package videoteca.main.Domain.Movie

import com.google.gson.annotations.SerializedName

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
