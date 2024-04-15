package videoteca.main

import android.content.Context
import android.content.SharedPreferences

class SharedInfo(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE)

    fun saveUserData(name: String, surname: String, uid:String) {
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("surname", surname)
        editor.putString("uid", uid)
        editor.apply()
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