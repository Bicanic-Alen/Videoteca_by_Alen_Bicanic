package videoteca.main.Adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import videoteca.main.Domain.GenreList
import videoteca.main.GenresActivity

import videoteca.main.R
import videoteca.main.api.AuthService
import videoteca.main.api.DatabaseManager

/**
 * Adapter per gestire l'elenco dei generi preferiti dell'utente.
 *
 * @param items Lista dei generi da visualizzare.
 */
class FavoriteGenreAdapter(private val items: GenreList): RecyclerView.Adapter<FavoriteGenreAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "GenreAdapter"

    /**
     * Crea un nuovo ViewHolder quando non ci sono viste esistenti che possono essere riutilizzate.
     *
     * @param parent Il ViewGroup nel quale la nuova View sarà aggiunta dopo essere stata associata a una posizione dell'adattatore.
     * @param viewType Il tipo di vista della nuova View.
     * @return Un nuovo ViewHolder che contiene una View del tipo di vista fornito.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteGenreAdapter.ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_genres_fav, parent, false)
        return ViewHolder(inflate)
    }

    /**
     * Associa i dati di un determinato elemento alla posizione specificata nella RecyclerView.
     *
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto dell'elemento nella posizione data nel set di dati.
     * @param position La posizione dell'elemento nel set di dati dell'adattatore.
     */
    override fun onBindViewHolder(holder: FavoriteGenreAdapter.ViewHolder, position: Int) {
        holder.textView.text = items.genres.get(position).name
        val idGenre = items.genres[position].id

        val uid = AuthService.getCurrentUser()?.uid
        val db = DatabaseManager()
        uid?.let { userId ->
            db.getFavoriteGenres(userId) { actualGenres ->
                val isPresent = actualGenres?.contains(idGenre) == true

                val context = holder.itemView.context

                val mainHandler = Handler(Looper.getMainLooper())

                // Esegui le operazioni UI nel thread principale
                mainHandler.post {
                    if (isPresent) {
                        holder.textView.setBackgroundResource(R.drawable.genre_background_selected_fav)
                    } else {
                        holder.textView.setBackgroundResource(R.drawable.genre_background_fav)
                    }

                    holder.textView.setOnClickListener {
                        db.getFavoriteGenres(userId){
                            val isPresentOnClick = it?.contains(idGenre) == true
                            if (isPresentOnClick) {
                                db.removeFavGenre(userId, idGenre)
                                holder.textView.setBackgroundResource(R.drawable.genre_background)

                            } else {
                                holder.textView.setBackgroundResource(R.drawable.genre_background_selected_fav)
                                db.addFavGenre(userId, idGenre)
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Restituisce il numero totale di elementi nell'elenco dei generi.
     *
     * @return Il numero totale di generi nel set di dati.
     */
    override fun getItemCount(): Int {
        return items.genres.size
    }

    /**
     * ViewHolder per contenere e gestire le viste degli elementi dell'elenco dei generi.
     *
     * @param itemView La vista radice che rappresenta un elemento dell'elenco.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        init {
            textView = itemView.findViewById(R.id.tv_genrename_fav)
        }
    }


}