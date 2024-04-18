package videoteca.main.gestioneAPI.Movie

data class MovieImages(
    var backdrops: List<ImageMovie> = emptyList(),
    var id: Int = 0,
    var logos: List<ImageMovie> = emptyList(),
    var posters: List<ImageMovie> = emptyList()
)