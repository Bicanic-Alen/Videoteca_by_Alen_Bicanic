package videoteca.main.gestioneAPI

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

class TMDB_Manager {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val TAG = "TMDBApiManger"

    private val apiKey = "94610c56d8ca01615eb4c8ad88a59d79"
    private val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NDYxMGM1NmQ4Y2EwMTYxNWViNGM4YWQ4OGE1OWQ3OSIsInN1YiI6IjY2MWQ5ZGEwNTI4YjJlMDE2NDNlNDA3ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.7lBtujdrJbwjditULHEySQ3zrF80lSsQz8JKAwYos7U"

    fun getMovieDiscover(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieDiscoverAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }

    fun getMoviePopular(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMoviePopularAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }

    fun getMovieTopRated(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieTopRatedAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }
    fun getMovieUpcomming(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieUpcommingAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }


    fun getMovieImage(movieId: Int, language: String, callback: (MovieImages?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieImageAsync(movieId, language)
            callback(response)
        }
    }


    fun getMovieDetails(movieId: Int, language: String, callback: (MovieDetails?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieDetailsAsync(movieId, language)
            callback(response)
        }
    }

    fun getMovieCredits(movieId: Int, language: String, callback: (CreditsMovie?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieCreditsAsync(movieId, language)
            callback(response)
        }
    }

    //Recommendations
    fun getMovieRecommendations(movieId : Int,language: String, callback: (MovieResponseRecommended?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieRecommendationsAsync(movieId, language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }

    fun getGenres(language: String, callback: (GenreList?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getGenresAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }


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

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonObject: JsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                val movieList = mutableListOf<Movie>()
                var page: Int = 0;
                page = jsonObject["page"].asInt
                Log.d(TAG, "Json Element: page = $page")
                val results = jsonObject["results"].asJsonArray
                for (jsonElementMovie in results) {
                    val jsonObjectMovie = jsonElementMovie.getAsJsonObject()
                    val adult = jsonObjectMovie["adult"].asBoolean
                    val backdrop_path = jsonObjectMovie["backdrop_path"].asString
                    val genre_ids = jsonObjectMovie["genre_ids"].asJsonArray
                    var gid = mutableListOf<Int>()
                    for (genres in genre_ids) {
                        gid.add(genres.asInt)
                    }
                    val id = jsonObjectMovie["id"].asInt
                    val originalLanguage = jsonObjectMovie["original_language"].asString
                    val originalTitle = jsonObjectMovie["original_title"].asString
                    val overview = jsonObjectMovie["overview"].asString
                    val popularity = jsonObjectMovie["popularity"].asDouble
                    val posterPath = jsonObjectMovie["poster_path"].asString
                    val releaseDate = jsonObjectMovie["release_date"].asString
                    val title = jsonObjectMovie["title"].asString
                    Log.d(TAG, "Json Element videoteca.main.gestioneAPI.Movie.Movie: title = $title")
                    val video = jsonObjectMovie["video"].asBoolean
                    val voteAverage = jsonObjectMovie["vote_average"].asDouble
                    val voteCount = jsonObjectMovie["vote_count"].asInt

                    val movie: Movie = Movie(
                        adult, backdrop_path, gid, id, originalLanguage,
                        originalTitle, overview, popularity, posterPath, releaseDate,
                        title, video, voteAverage, voteCount
                    )
                    movieList.add(movie)

                }
                val totalPages = jsonObject["total_pages"].asInt
                val totalResult = jsonObject["total_results"].asInt
                MovieResponse(page, movieList, totalPages,totalResult)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }

    private suspend fun getMovieDiscoverAsync(language: String): MovieResponse? {
        return try {
            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=true&language=$language&page=1&sort_by=popularity.desc")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonObject: JsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                val movieList = mutableListOf<Movie>()
                var page: Int = 0;
                page = jsonObject["page"].asInt
                Log.d(TAG, "Json Element: page = $page")
                val results = jsonObject["results"].asJsonArray
                for (jsonElementMovie in results) {
                    val jsonObjectMovie = jsonElementMovie.getAsJsonObject()
                    val adult = jsonObjectMovie["adult"].asBoolean
                    val backdrop_path = jsonObjectMovie["backdrop_path"].asString
                    val genre_ids = jsonObjectMovie["genre_ids"].asJsonArray
                    var gid = mutableListOf<Int>()
                    for (genres in genre_ids) {
                        gid.add(genres.asInt)
                    }
                    val id = jsonObjectMovie["id"].asInt
                    val originalLanguage = jsonObjectMovie["original_language"].asString
                    val originalTitle = jsonObjectMovie["original_title"].asString
                    val overview = jsonObjectMovie["overview"].asString
                    val popularity = jsonObjectMovie["popularity"].asDouble
                    val posterPath = jsonObjectMovie["poster_path"].asString
                    val releaseDate = jsonObjectMovie["release_date"].asString
                    val title = jsonObjectMovie["title"].asString
                    Log.d(TAG, "Json Element videoteca.main.gestioneAPI.Movie.Movie: title = $title")
                    val video = jsonObjectMovie["video"].asBoolean
                    val voteAverage = jsonObjectMovie["vote_average"].asDouble
                    val voteCount = jsonObjectMovie["vote_count"].asInt

                    val movie: Movie = Movie(
                        adult, backdrop_path, gid, id, originalLanguage,
                        originalTitle, overview, popularity, posterPath, releaseDate,
                        title, video, voteAverage, voteCount
                    )
                    movieList.add(movie)

                }
                val totalPages = jsonObject["total_pages"].asInt
                val totalResult = jsonObject["total_results"].asInt
                MovieResponse(page, movieList, totalPages,totalResult)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }

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

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonObject: JsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                val movieList = mutableListOf<Movie>()
                var page: Int = 0;
                page = jsonObject["page"].asInt
                Log.d(TAG, "Json Element: page = $page")
                val results = jsonObject["results"].asJsonArray
                for (jsonElementMovie in results) {
                    val jsonObjectMovie = jsonElementMovie.getAsJsonObject()
                    val adult = jsonObjectMovie["adult"].asBoolean
                    val backdrop_path = jsonObjectMovie["backdrop_path"].asString
                    val genre_ids = jsonObjectMovie["genre_ids"].asJsonArray
                    var gid = mutableListOf<Int>()
                    for (genres in genre_ids) {
                        gid.add(genres.asInt)
                    }
                    val id = jsonObjectMovie["id"].asInt
                    val originalLanguage = jsonObjectMovie["original_language"].asString
                    val originalTitle = jsonObjectMovie["original_title"].asString
                    val overview = jsonObjectMovie["overview"].asString
                    val popularity = jsonObjectMovie["popularity"].asDouble
                    val posterPath = jsonObjectMovie["poster_path"].asString
                    val releaseDate = jsonObjectMovie["release_date"].asString
                    val title = jsonObjectMovie["title"].asString
                    Log.d(TAG, "Json Element videoteca.main.gestioneAPI.Movie.Movie: title = $title")
                    val video = jsonObjectMovie["video"].asBoolean
                    val voteAverage = jsonObjectMovie["vote_average"].asDouble
                    val voteCount = jsonObjectMovie["vote_count"].asInt

                    val movie: Movie = Movie(
                        adult, backdrop_path, gid, id, originalLanguage,
                        originalTitle, overview, popularity, posterPath, releaseDate,
                        title, video, voteAverage, voteCount
                    )
                    movieList.add(movie)

                }
                val totalPages = jsonObject["total_pages"].asInt
                val totalResult = jsonObject["total_results"].asInt
                MovieResponse(page, movieList, totalPages,totalResult)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }


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

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonObject: JsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                val movieList = mutableListOf<Movie>()
                var page: Int = 0;
                page = jsonObject["page"].asInt
                Log.d(TAG, "Json Element: page = $page")
                val results = jsonObject["results"].asJsonArray
                for (jsonElementMovie in results) {
                    val jsonObjectMovie = jsonElementMovie.getAsJsonObject()
                    val adult = jsonObjectMovie["adult"].asBoolean
                    val backdrop_path = jsonObjectMovie["backdrop_path"].asString
                    val genre_ids = jsonObjectMovie["genre_ids"].asJsonArray
                    var gid = mutableListOf<Int>()
                    for (genres in genre_ids) {
                        gid.add(genres.asInt)
                    }
                    val id = jsonObjectMovie["id"].asInt
                    val originalLanguage = jsonObjectMovie["original_language"].asString
                    val originalTitle = jsonObjectMovie["original_title"].asString
                    val overview = jsonObjectMovie["overview"].asString
                    val popularity = jsonObjectMovie["popularity"].asDouble
                    val posterPath = jsonObjectMovie["poster_path"].asString
                    val releaseDate = jsonObjectMovie["release_date"].asString
                    val title = jsonObjectMovie["title"].asString
                    Log.d(TAG, "Json Element videoteca.main.gestioneAPI.Movie.Movie: title = $title")
                    val video = jsonObjectMovie["video"].asBoolean
                    val voteAverage = jsonObjectMovie["vote_average"].asDouble
                    val voteCount = jsonObjectMovie["vote_count"].asInt

                    val movie: Movie = Movie(
                        adult, backdrop_path, gid, id, originalLanguage,
                        originalTitle, overview, popularity, posterPath, releaseDate,
                        title, video, voteAverage, voteCount
                    )
                    movieList.add(movie)

                }
                val totalPages = jsonObject["total_pages"].asInt
                val totalResult = jsonObject["total_results"].asInt
                MovieResponse(page, movieList, totalPages,totalResult)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies", e)
            null
        }
    }

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
