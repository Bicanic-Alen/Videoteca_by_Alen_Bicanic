package videoteca.main.Domain.Movie

import com.google.gson.annotations.SerializedName
data class Image(
    @SerializedName("aspect_ratio") val aspectRatio: Double = 0.0,
    @SerializedName("height") val height: Int = 0,
    @SerializedName("iso_639_1") val iso: String = "",
    @SerializedName("file_path") val filePath: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("width") val width: Int = 0
) {
    // Costruttore vuoto
    constructor() : this(0.0, 0, "", "", 0.0, 0, 0)
}

data class MovieImages(
    @SerializedName("id") val movieId: Int = 0,
    @SerializedName("backdrops") val backdrops: List<Image> = listOf(),
    @SerializedName("logos") val logos: List<Image> = listOf(),
    @SerializedName("posters") val posters: List<Image> = listOf()
) {
    // Costruttore vuoto
    constructor() : this(0, listOf(), listOf(), listOf())
}
