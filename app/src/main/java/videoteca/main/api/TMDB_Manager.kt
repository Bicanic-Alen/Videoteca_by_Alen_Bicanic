package videoteca.main.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import videoteca.main.Domain.GenreList
import videoteca.main.Domain.Movie.CreditsMovie
import videoteca.main.Domain.Movie.MovieDetails
import videoteca.main.Domain.Movie.MovieResponse.Movie
import videoteca.main.Domain.Movie.MovieImages
import videoteca.main.Domain.Movie.MovieResponse
import videoteca.main.Domain.Movie.MovieResponseRecommended

import kotlin.Exception

/**
 * Classe che gestisce l'interazione con l'API di The Movie Database (TMDB).
 * Fornisce metodi per ottenere informazioni sui film, come film popolari, film più votati, dettagli dei film, immagini e altro.
 * Utilizza OkHttpClient per effettuare richieste HTTP e Gson per deserializzare le risposte JSON.
 */
class TMDB_Manager {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val TAG = "TMDBApiManger"

    private val apiKey = "94610c56d8ca01615eb4c8ad88a59d79"
    private val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NDYxMGM1NmQ4Y2EwMTYxNWViNGM4YWQ4OGE1OWQ3OSIsInN1YiI6IjY2MWQ5ZGEwNTI4YjJlMDE2NDNlNDA3ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.7lBtujdrJbwjditULHEySQ3zrF80lSsQz8JKAwYos7U"

    /**
     * Ottiene i film da scoprire in base alla lingua.
     * @param language La lingua in cui ottenere i film.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMovieDiscover(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieDiscoverAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }

    /**
     * Ottiene i film da scoprire in base alla lingua, al genere e alla pagina.
     * @param language La lingua in cui ottenere i film.
     * @param genreId L'ID del genere del film.
     * @param page La pagina dei risultati.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMovieDiscover(language: String, genreId: Int, page: Int,callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieDiscoverAsync(language, genreId, page)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }


    /**
     * Ottiene i film da scoprire in base alla lingua, alla lista dei generi e alla pagina.
     * @param language La lingua in cui ottenere i film.
     * @param genreIdList La lista degli ID dei generi del film.
     * @param page La pagina dei risultati.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMovieDiscover(language: String, genreIdList: List<Int>, page: Int,callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieDiscoverAsync(language, genreIdList, page)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }



    /**
     * Ottiene i film popolari in base alla lingua.
     * @param language La lingua in cui ottenere i film.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMoviePopular(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMoviePopularAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }

    /**
     * Ottiene i film più votati in base alla lingua.
     * @param language La lingua in cui ottenere i film.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMovieTopRated(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieTopRatedAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }


    /**
     * Ottiene i film in uscita in base alla lingua.
     * @param language La lingua in cui ottenere i film.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMovieUpcomming(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieUpcommingAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }


    /**
     * Ottiene le immagini di un film in base all'ID del film e alla lingua.
     * @param movieId L'ID del film.
     * @param language La lingua delle immagini.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMovieImage(movieId: Int, language: String, callback: (MovieImages?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieImageAsync(movieId, language)
            callback(response)
        }
    }


    /**
     * Ottiene i dettagli di un film in base all'ID del film e alla lingua.
     * @param movieId L'ID del film.
     * @param language La lingua dei dettagli.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMovieDetails(movieId: Int, language: String, callback: (MovieDetails?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieDetailsAsync(movieId, language)
            callback(response)
        }
    }

    /**
     * Ottiene i crediti di un film in base all'ID del film e alla lingua.
     * @param movieId L'ID del film.
     * @param language La lingua dei crediti.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMovieCredits(movieId: Int, language: String, callback: (CreditsMovie?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieCreditsAsync(movieId, language)
            callback(response)
        }
    }

    /**
     * Ottiene i film consigliati in base all'ID del film e alla lingua.
     * @param movieId L'ID del film.
     * @param language La lingua dei film consigliati.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getMovieRecommendations(movieId : Int,language: String, callback: (MovieResponseRecommended?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieRecommendationsAsync(movieId, language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }

    /**
     * Ottiene i generi disponibili in base alla lingua.
     * @param language La lingua dei generi.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun getGenres(language: String, callback: (GenreList?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getGenresAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }

    /**
     * Cerca un film in base alla query e alla lingua.
     * @param query La query di ricerca.
     * @param language La lingua dei risultati di ricerca.
     * @param callback La funzione di callback da eseguire quando la risposta è pronta.
     */
    fun searchMovie(query:String, language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = searchMovieAsync(query,language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }


    // Metodo privato per cercare un film in modo asincrono
    private fun searchMovieAsync(query: String, language: String): MovieResponse? {
        return try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/search/movie?query=${query}&include_adult=false&language=${language}&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NDYxMGM1NmQ4Y2EwMTYxNWViNGM4YWQ4OGE1OWQ3OSIsInN1YiI6IjY2MWQ5ZGEwNTI4YjJlMDE2NDNlNDA3ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.7lBtujdrJbwjditULHEySQ3zrF80lSsQz8JKAwYos7U")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val movieResponse = Gson().fromJson(responseBody, MovieResponse::class.java)
            movieResponse
        }catch (e: Exception){
            Log.e(TAG, "error fetching search",e)
            null
        }
    }


    // Metodo privato per ottenere i generi in modo asincrono
    private suspend fun getGenresAsync(language:String):GenreList?{
        return try{
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/genre/movie/list?language=${language}")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer ${accessToken}")
                .build()

            val response = client.newCall(request).execute()

            val responseBody = response.body?.string()
            val genreList = Gson().fromJson(responseBody, GenreList::class.java)

            Log.d(TAG, "Success fetching genre list: $genreList")
            genreList

        }catch (e:Exception){
            Log.e(TAG, "Error fetching genre list", e)
            null
        }
    }

    // Metodo privato per ottenere i film consigliati in modo asincrono
    private suspend fun getMovieRecommendationsAsync(movieId: Int, language: String): MovieResponseRecommended? {
        return try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/${movieId}/recommendations?language=${language}&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer ${accessToken}")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val movieResponseRecommended = Gson().fromJson(responseBody, MovieResponseRecommended::class.java)
            movieResponseRecommended
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }


    // Metodo privato per ottenere i film da scoprire in base al genere e alla pagina in modo asincrono
    private suspend fun getMovieDiscoverAsync(language: String, genreId: Int, page:Int): MovieResponse? {
        return try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=${language}&page=${page}&sort_by=popularity.desc&with_genres=${genreId}")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val movieResponse = Gson().fromJson(responseBody, MovieResponse::class.java)
            movieResponse

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }

    // Metodo privato per ottenere i film da scoprire in base alla lista dei generi e alla pagina in modo asincrono
    private suspend fun getMovieDiscoverAsync(language: String, genreIdList: List<Int>, page:Int): MovieResponse? {
        return try {

            var stringGenresids = ""
            for (id in genreIdList){
                stringGenresids += "${id}-"
            }

            stringGenresids = stringGenresids.dropLast(1)

            Log.d(TAG, "stringa dei generi: $stringGenresids")

            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=${language}&page=${page}&sort_by=popularity.desc&with_genres=${stringGenresids}")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val movieResponse = Gson().fromJson(responseBody, MovieResponse::class.java)
            movieResponse

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }

    // Metodo privato per ottenere i film popolari in modo asincrono
    private suspend fun getMoviePopularAsync(language: String): MovieResponse? {
        return try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/popular?language=${language}-US&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()

            val responseBody = response.body?.string()
            val movieResponse = Gson().fromJson(responseBody, MovieResponse::class.java)
            movieResponse

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }

    // Metodo privato per ottenere i film da scoprire in base alla lingua in modo asincrono
    private suspend fun getMovieDiscoverAsync(language: String): MovieResponse? {
        return try {
            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=true&language=$language&page=1&sort_by=popularity.desc")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()

            val responseBody = response.body?.string()
            val movieResponse = Gson().fromJson(responseBody, MovieResponse::class.java)
            movieResponse
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }

    // Metodo privato per ottenere i film più votati in modo asincrono
    private suspend fun getMovieTopRatedAsync(language: String): MovieResponse? {
        return try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/top_rated?language=${language}&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()

            val responseBody = response.body?.string()
            val movieResponse = Gson().fromJson(responseBody, MovieResponse::class.java)
            movieResponse
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }


    // Metodo privato per ottenere i film in arrivo in modo asincrono
    private suspend fun getMovieUpcommingAsync(language: String): MovieResponse? {
        return try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/upcoming?language=${language}&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()

            val responseBody = response.body?.string()
            val movieResponse = Gson().fromJson(responseBody, MovieResponse::class.java)
            movieResponse
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }

    // Metodo privato per ottenere le immagini di un film in modo asincrono
    private suspend fun getMovieImageAsync(movieId: Int, language: String): MovieImages? {
        return try {
            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/$movieId/images?include_image_language=$language")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val movieImages = Gson().fromJson(responseBody, MovieImages::class.java)
                movieImages
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movie images", e)
            null
        }
    }


    // Metodo privato per ottenere i dettagli di un film in modo asincrono
    private suspend fun getMovieDetailsAsync(movieId: Int, language: String): MovieDetails? {
        return try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/${movieId}?append_to_response=videos&language=${language}")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer ${accessToken}")
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val movieDetails = Gson().fromJson(responseBody, MovieDetails::class.java)

                movieDetails //oggetto restituito
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }


    // Metodo privato per ottenere i crediti di un film in modo asincrono
    private suspend fun getMovieCreditsAsync(movieId: Int, language:String): CreditsMovie?{

        return try{
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/${movieId}/credits?language=${language}")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer ${accessToken}")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val creditsMovie = Gson().fromJson(responseBody, CreditsMovie::class.java)
            creditsMovie

        }catch (e: Exception){
            Log.e(TAG, "Error fetching movie credits", e)
            return null
        }
    }

}
