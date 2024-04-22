package videoteca.main.Domain

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName

data class UserDB(

    @SerializedName("name") var name: String = "",
    @SerializedName("surname") var surname: String = "",
    @SerializedName("birthDate") var birthDate: Timestamp? = null,
    @SerializedName("email") var email: String = "",
    @SerializedName("favGenres") var favGenres: List<String> = emptyList(),
    @SerializedName("favMovies") var favMovies: List<Int> = emptyList(),
    @SerializedName("rentedMovies") var rentedMovies: List<String> = emptyList()

) {
    constructor() : this("", "", null, "", emptyList(), emptyList(), emptyList())
}



