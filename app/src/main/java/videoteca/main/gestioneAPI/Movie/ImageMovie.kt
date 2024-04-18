package videoteca.main.gestioneAPI.Movie
data class ImageMovie(
    var aspect_ratio: Double = 0.0,
    var height: Int = 0,
    var iso_639_1: String = "",
    var file_path: String = "",
    var vote_average: Double = 0.0,
    var vote_count: Int = 0,
    var width: Int = 0
)