package videoteca.main.Domain

import java.util.Date

data class MovieRentedInfo(
    val id: Int = 0,
    val dateExpiration: Date? = null,
    val posterPath:String = "",
    val title:String = "",
    val dateMovie: String = ""
    ){

    constructor():this(0,null,"", "", "")
}
