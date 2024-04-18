package videoteca.main.gestioneAPI

import videoteca.main.gestioneAPI.Movie.ImageSize
import videoteca.main.gestioneAPI.Movie.LogoSize
import videoteca.main.gestioneAPI.Movie.PosterSize
import videoteca.main.gestioneAPI.Movie.ProfileSize
import videoteca.main.gestioneAPI.Movie.StillSize

class TMDB_ImageManager {
    private val base_url = "https://image.tmdb.org/t/p"

    fun getBaseUrl():String{
        return base_url
    }

    fun buildImageUrl(size: ImageSize, filePath: String): String {
        return "$base_url/${size.value}/$filePath"
    }

    fun buildImageUrl(size: PosterSize, filePath: String): String {
        return "$base_url/${size.value}/$filePath"
    }

    fun buildImageUrl(size: LogoSize, filePath: String): String {
        return "$base_url/${size.value}/$filePath"
    }

    fun buildImageUrl(size: ProfileSize, filePath: String): String {
        return "$base_url/${size.value}/$filePath"
    }

    fun buildImageUrl(size: StillSize, filePath: String): String {
        return "$base_url/${size.value}/$filePath"
    }



}

