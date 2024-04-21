package videoteca.main.Domain

import com.google.firebase.Timestamp

data class UserDB(
    var id: String = "",
    var name: String = "",
    var surname: String = "",
    var birthDate: Timestamp? = null,
    var email: String = "",
    var favGenres: List<String> = emptyList(),
    var rentedMovies: List<String> = emptyList(),
    var reservedMovies: List<String> = emptyList()
) {
    constructor() : this("", "", "", null, "", emptyList(), emptyList(), emptyList())
}



