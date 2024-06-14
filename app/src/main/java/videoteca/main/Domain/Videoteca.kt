package videoteca.main.Domain

/**
 * Rappresenta un oggetto Videoteca, che contiene informazioni relative a un video associato a un film.
 *
 * @property id L'ID univoco della voce nella videoteca.
 * @property id_movie L'ID del film associato al video.
 * @property video_path Il percorso del video all'interno della videoteca.
 * @constructor Crea un'istanza di Videoteca con valori predefiniti.
 */

data class Videoteca(
    val id: Long = 0,
    val id_movie: Long = 0,
    val video_path: String = ""
){
    /**
     * Costruttore secondario vuoto che inizializza un'istanza di Videoteca con valori predefiniti.
     */
    constructor() :this(0,0,"")
}
