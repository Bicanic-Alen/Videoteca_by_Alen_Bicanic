package videoteca.main.Domain

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName

data class UserDB(

    @SerializedName("name") val name: String = "",
    @SerializedName("surname") val surname: String = "",
    @SerializedName("birthDate") val birthDate: Timestamp? = null,
    @SerializedName("email") val email: String = "",
    @SerializedName("favGenres") val favGenres: List<String> = emptyList(),
    @SerializedName("favMovies") val favMovies: List<Int> = emptyList(),
    @SerializedName("watchlist") val watchlist: List<Int> = emptyList(),
    @SerializedName("rentedMovies") val rentedMovies: List<RentedMoviesInfo> = emptyList()

) { constructor() : this("", "", null, "", emptyList(), emptyList(), emptyList(), emptyList())

    data class RentedMoviesInfo(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("rentDay") val rentDay:Timestamp? = null
    ){
        constructor():this(0,null)
    }

}



