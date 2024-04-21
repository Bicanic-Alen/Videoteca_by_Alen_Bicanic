package videoteca.main.Domain.Movie

data class MovieResponse(
    val page: Int = 0,
    val results: List<Movie> = emptyList(),
    val totalPage: Int = 0,
    val totalResult: Int = 0
) {
    data class Movie(
        val adult: Boolean = false,
        val backdropPath: String = "",
        val genreIds: List<Int> = emptyList(),
        val id: Int = 0,
        val originalLanguage: String = "",
        val originalTitle: String = "",
        val overview: String = "",
        val popularity: Double = 0.0,
        val posterPath: String = "",
        val releaseDate: String = "",
        val title: String = "",
        val video: Boolean = false,
        val voteAverage: Double = 0.0,
        val voteCount: Int = 0
    )
}

