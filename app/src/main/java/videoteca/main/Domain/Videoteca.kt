package videoteca.main.Domain

data class Videoteca(
    val id: Long = 0,
    val id_movie: Long = 0,
    val video_path: String = ""
){
    constructor() :this(0,0,"")
}
