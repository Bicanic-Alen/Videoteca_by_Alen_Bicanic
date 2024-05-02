package videoteca.main

import android.content.Context
import android.content.SharedPreferences

class SharedInfo(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE)
    private val sharedPreferencesM: SharedPreferences = context.getSharedPreferences("movieTime", Context.MODE_PRIVATE)

    fun saveUserData(name: String, surname: String, uid:String) {
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("surname", surname)
        editor.putString("uid", uid)
        editor.apply()
    }

    fun saveMovieTime(movieid: Int, saveTime:Long){
        val editor = sharedPreferencesM.edit()
        editor.putLong("movie_${movieid}_time", saveTime)
        editor.apply()
    }

    fun removeMovieTime(movieid: Int) {
        val editor = sharedPreferencesM.edit()
        editor.remove("movie_${movieid}_time")
        editor.apply()
    }

    fun getMovieTime(movieid: Int):Long{
        return sharedPreferencesM.getLong("movie_${movieid}_time",0L)
    }



    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove("name")
        editor.remove("surname")
        editor.apply()
    }

    fun getUserName(): String? {
        return sharedPreferences.getString("name", null)
    }

    fun getUserSurname(): String? {
        return sharedPreferences.getString("surname", null)
    }

    fun getUserUID(): String? {
        return sharedPreferences.getString("uid", null)
    }




}