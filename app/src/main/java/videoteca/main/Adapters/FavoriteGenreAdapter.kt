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


class FavoriteGenreAdapter(private val items: GenreList): RecyclerView.Adapter<FavoriteGenreAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "GenreAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteGenreAdapter.ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_genres_fav, parent, false)
        return ViewHolder(inflate)
    }

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

    override fun getItemCount(): Int {
        return items.genres.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        init {
            textView = itemView.findViewById(R.id.tv_genrename_fav)
        }
    }


}