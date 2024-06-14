package videoteca.main.api

import videoteca.main.Domain.Movie.ImageSize
import videoteca.main.Domain.Movie.LogoSize
import videoteca.main.Domain.Movie.PosterSize
import videoteca.main.Domain.Movie.ProfileSize
import videoteca.main.Domain.Movie.StillSize

/**
 * Questa classe gestisce la costruzione degli URL delle immagini per l'API di TMDB.
 */
class TMDB_ImageManager {
    private val base_url = "https://image.tmdb.org/t/p"

    /**
     * Restituisce l'URL base per le immagini di TMDB.
     *
     * @return L'URL base come stringa.
     */
    fun getBaseUrl():String{
        return base_url
    }

    /**
     * Costruisce l'URL dell'immagine utilizzando la dimensione specificata e il percorso del file.
     *
     * @param size La dimensione dell'immagine desiderata.
     * @param filePath Il percorso del file dell'immagine.
     * @return L'URL completo dell'immagine.
     */
    fun buildImageUrl(size: ImageSize, filePath: String?): String{
        return if(filePath !=null){
            "$base_url/${size.value}/$filePath"
        } else{
            ""
        }
    }

    /**
     * Costruisce l'URL dell'immagine del poster utilizzando la dimensione specificata e il percorso del file.
     *
     * @param size La dimensione del poster desiderata.
     * @param filePath Il percorso del file del poster.
     * @return L'URL completo del poster.
     */
    fun buildImageUrl(size: PosterSize, filePath: String?): String {
        return if(filePath !=null){
            "$base_url/${size.value}/$filePath"
        } else{
            ""
        }
    }

    /**
     * Costruisce l'URL dell'immagine del logo utilizzando la dimensione specificata e il percorso del file.
     *
     * @param size La dimensione del logo desiderata.
     * @param filePath Il percorso del file del logo.
     * @return L'URL completo del logo.
     */
    fun buildImageUrl(size: LogoSize, filePath: String?): String {
        return if(filePath !=null){
            "$base_url/${size.value}/$filePath"
        } else{
            ""
        }
    }

    /**
     * Costruisce l'URL dell'immagine del profilo utilizzando la dimensione specificata e il percorso del file.
     *
     * @param size La dimensione del profilo desiderata.
     * @param filePath Il percorso del file del profilo.
     * @return L'URL completo del profilo.
     */
    fun buildImageUrl(size: ProfileSize, filePath: String?): String {

        return if(filePath !=null){
            "$base_url/${size.value}/$filePath"
        } else{
            ""
        }
    }

    /**
     * Costruisce l'URL dell'immagine still utilizzando la dimensione specificata e il percorso del file.
     *
     * @param size La dimensione still desiderata.
     * @param filePath Il percorso del file still.
     * @return L'URL completo dello still.
     */
    fun buildImageUrl(size: StillSize, filePath: String?): String {
        return if(filePath !=null){
            "$base_url/${size.value}/$filePath"
        } else{
            ""
        }
    }



}

