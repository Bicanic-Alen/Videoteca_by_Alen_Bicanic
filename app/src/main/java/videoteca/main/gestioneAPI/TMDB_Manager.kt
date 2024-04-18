package videoteca.main.gestioneAPI

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import videoteca.main.gestioneAPI.Movie.ImageMovie
import videoteca.main.gestioneAPI.Movie.MovieResponse.Movie
import videoteca.main.gestioneAPI.Movie.MovieImages
import videoteca.main.gestioneAPI.Movie.MovieResponse
import java.lang.Exception

class TMDB_Manager {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val TAG = "TMDBApiManger"

    private val apiKey = "94610c56d8ca01615eb4c8ad88a59d79"
    private val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NDYxMGM1NmQ4Y2EwMTYxNWViNGM4YWQ4OGE1OWQ3OSIsInN1YiI6IjY2MWQ5ZGEwNTI4YjJlMDE2NDNlNDA3ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.7lBtujdrJbwjditULHEySQ3zrF80lSsQz8JKAwYos7U"

    fun movieDiscover(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieDiscoverAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }

    fun moviePopular(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMoviePopularAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }

    fun movieTopRated(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieTopRatedAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }
    fun movieUpcomming(language: String, callback: (MovieResponse?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieUpcommingAsync(language)
            Log.d(TAG, "response : $response")
            callback(response)
        }
    }


    fun movieImage(movieId: Int, language: String, callback: (MovieImages?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getMovieImageAsync(movieId, language)
            callback(response)
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
                .url("https://api.themoviedb.org/3/movie/$movieId/images?include_image_language=$language&language=en")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonObjectBase: JsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                val backdrops = mutableListOf<ImageMovie>()
                for (backdrop in jsonObjectBase["backdrops"].asJsonArray) {
                    val bd = backdrop.asJsonObject
                    backdrops.add(
                        ImageMovie(
                            bd["aspect_ratio"].asDouble,
                            bd["height"].asInt,
                            bd["iso_639_1"].asString,
                            bd["file_path"].asString,
                            bd["vote_average"].asDouble,
                            bd["vote_count"].asInt,
                            bd["width"].asInt
                        )
                    )
                }
                val id = jsonObjectBase["id"].asInt
                val posters = mutableListOf<ImageMovie>()
                for (poster in jsonObjectBase["posters"].asJsonArray) {
                    val pr = poster.asJsonObject;
                    posters.add(
                        ImageMovie(
                            pr["aspect_ratio"].asDouble,
                            pr["height"].asInt,
                            pr["iso_639_1"].asString,
                            pr["file_path"].asString,
                            pr["vote_average"].asDouble,
                            pr["vote_count"].asInt,
                            pr["width"].asInt
                        )
                    )
                }
                val logos = mutableListOf<ImageMovie>()
                for (logo in jsonObjectBase.asJsonArray) {
                    val lg = logo.asJsonObject
                    logos.add(
                        ImageMovie(
                            lg["aspect_ratio"].asDouble,
                            lg["height"].asInt,
                            lg["iso_639_1"].asString,
                            lg["file_path"].asString,
                            lg["vote_average"].asDouble,
                            lg["vote_count"].asInt,
                            lg["width"].asInt
                        )
                    )
                }
                MovieImages(backdrops, id, logos, posters)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movie images", e)
            null
        }
    }
}
