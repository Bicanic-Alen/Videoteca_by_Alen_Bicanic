package videoteca.main.Domain

import com.google.gson.annotations.SerializedName

data class Videoteca(
     val id: Long,
     @SerializedName("id_movie") val idMovie: Int = 0,
     @SerializedName("video_path") val videoPath: String = ""
){
    constructor() : this(0, 0, "")
}
