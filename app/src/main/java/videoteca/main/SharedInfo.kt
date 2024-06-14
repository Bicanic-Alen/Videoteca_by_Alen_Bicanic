package videoteca.main

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestisce la memorizzazione e il recupero delle informazioni utente e dei tempi di visione dei film.
 *
 * @property context Il contesto dell'applicazione.
 */
class SharedInfo(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE)
    private val sharedPreferencesM: SharedPreferences = context.getSharedPreferences("movieTime", Context.MODE_PRIVATE)

    /**
     * Salva i dati dell'utente nelle SharedPreferences.
     *
     * @param name Il nome dell'utente.
     * @param surname Il cognome dell'utente.
     * @param uid L'ID dell'utente.
     */
    fun saveUserData(name: String, surname: String, uid:String) {
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("surname", surname)
        editor.putString("uid", uid)
        editor.apply()
    }

    /**
     * Salva il tempo di visione di un film nelle SharedPreferences.
     *
     * @param movieid L'ID del film.
     * @param saveTime Il tempo di visione da salvare in millisecondi.
     */
    fun saveMovieTime(movieid: Int, saveTime:Long){
        val editor = sharedPreferencesM.edit()
        editor.putLong("movie_${movieid}_time", saveTime)
        editor.apply()
    }

    /**
     * Rimuove il tempo di visione di un film dalle SharedPreferences.
     *
     * @param movieid L'ID del film.
     */
    fun removeMovieTime(movieid: Int) {
        val editor = sharedPreferencesM.edit()
        editor.remove("movie_${movieid}_time")
        editor.apply()
    }

    /**
     * Recupera il tempo di visione di un film dalle SharedPreferences.
     *
     * @param movieid L'ID del film.
     * @return Il tempo di visione in millisecondi. Ritorna 0 se non è presente.
     */
    fun getMovieTime(movieid: Int):Long{
        return sharedPreferencesM.getLong("movie_${movieid}_time",0L)
    }


    /**
     * Cancella i dati dell'utente dalle SharedPreferences.
     */
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove("name")
        editor.remove("surname")
        editor.apply()
    }

    /**
     * Recupera il nome dell'utente dalle SharedPreferences.
     *
     * @return Il nome dell'utente, oppure null se non è presente.
     */
    fun getUserName(): String? {
        return sharedPreferences.getString("name", null)
    }

    /**
     * Recupera il cognome dell'utente dalle SharedPreferences.
     *
     * @return Il cognome dell'utente, oppure null se non è presente.
     */
    fun getUserSurname(): String? {
        return sharedPreferences.getString("surname", null)
    }

    /**
     * Recupera l'UID dell'utente dalle SharedPreferences.
     *
     * @return L'UID dell'utente, oppure null se non è presente.
     */
    fun getUserUID(): String? {
        return sharedPreferences.getString("uid", null)
    }




}