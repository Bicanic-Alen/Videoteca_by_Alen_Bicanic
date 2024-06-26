package videoteca.main.Domain.Movie


/**
 * Enumerazione che rappresenta le dimensioni disponibili per le immagini.
 *
 * @property value Valore della dimensione dell'immagine utilizzato nelle chiamate API.
 * @constructor Crea un'istanza di ImageSize con il valore specificato.
 */
enum class ImageSize(val value: String) {
    W300("w300"),
    W780("w780"),
    W1280("w1280"),
    ORIGINAL("original")
}

/**
 * Enumerazione che rappresenta le dimensioni disponibili per i loghi.
 *
 * @property value Valore della dimensione del logo utilizzato nelle chiamate API.
 * @constructor Crea un'istanza di LogoSize con il valore specificato.
 */
enum class LogoSize(val value: String) {
    W45("w45"),
    W92("w92"),
    W154("w154"),
    W185("w185"),
    W300("w300"),
    W500("w500"),
    ORIGINAL("original")
}

/**
 * Enumerazione che rappresenta le dimensioni disponibili per i poster.
 *
 * @property value Valore della dimensione del poster utilizzato nelle chiamate API.
 * @constructor Crea un'istanza di PosterSize con il valore specificato.
 */
enum class PosterSize(val value: String) {
    W92("w92"),
    W154("w154"),
    W185("w185"),
    W342("w342"),
    W500("w500"),
    W780("w780"),
    ORIGINAL("original")
}

/**
 * Enumerazione che rappresenta le dimensioni disponibili per i profili.
 *
 * @property value Valore della dimensione del profilo utilizzato nelle chiamate API.
 * @constructor Crea un'istanza di ProfileSize con il valore specificato.
 */
enum class ProfileSize(val value: String) {
    W45("w45"),
    W185("w185"),
    H632("h632"),
    ORIGINAL("original")
}

/**
 * Enumerazione che rappresenta le dimensioni disponibili per le still image.
 *
 * @property value Valore della dimensione della still image utilizzato nelle chiamate API.
 * @constructor Crea un'istanza di StillSize con il valore specificato.
 */
enum class StillSize(val value: String) {
    W92("w92"),
    W185("w185"),
    W300("w300"),
    ORIGINAL("original")
}

/**
 * Enumerazione che rappresenta le chiavi di modifica per le richieste di cambiamento.
 *
 * @property key Chiave di modifica utilizzata nelle richieste di cambio.
 * @constructor Crea un'istanza di ChangeKey con la chiave specificata.
 */
enum class ChangeKey(val key: String) {
    ADULT("adult"),
    AIR_DATE("air_date"),
    ALSO_KNOWN_AS("also_known_as"),
    ALTERNATIVE_TITLES("alternative_titles"),
    BIOGRAPHY("biography"),
    BIRTHDAY("birthday"),
    BUDGET("budget"),
    CAST("cast"),
    CERTIFICATIONS("certifications"),
    CHARACTER_NAMES("character_names"),
    CREATED_BY("created_by"),
    CREW("crew"),
    DEATHDAY("deathday"),
    EPISODE("episode"),
    EPISODE_NUMBER("episode_number"),
    EPISODE_RUN_TIME("episode_run_time"),
    FREEBASE_ID("freebase_id"),
    FREEBASE_MID("freebase_mid"),
    GENERAL("general"),
    GENRES("genres"),
    GUEST_STARS("guest_stars"),
    HOMEPAGE("homepage"),
    IMAGES("images"),
    IMDB_ID("imdb_id"),
    LANGUAGES("languages"),
    NAME("name"),
    NETWORK("network"),
    ORIGIN_COUNTRY("origin_country"),
    ORIGINAL_NAME("original_name"),
    ORIGINAL_TITLE("original_title"),
    OVERVIEW("overview"),
    PARTS("parts"),
    PLACE_OF_BIRTH("place_of_birth"),
    PLOT_KEYWORDS("plot_keywords"),
    PRODUCTION_CODE("production_code"),
    PRODUCTION_COMPANIES("production_companies"),
    PRODUCTION_COUNTRIES("production_countries"),
    RELEASES("releases"),
    REVENUE("revenue"),
    RUNTIME("runtime"),
    SEASON("season"),
    SEASON_NUMBER("season_number"),
    SEASON_REGULAR("season_regular"),
    SPOKEN_LANGUAGES("spoken_languages"),
    STATUS("status"),
    TAGLINE("tagline"),
    TITLE("title"),
    TRANSLATIONS("translations"),
    TVDB_ID("tvdb_id"),
    TVRAGE_ID("tvrage_id"),
    TYPE("type"),
    VIDEO("video"),
    VIDEOS("videos")
}
