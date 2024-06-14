package videoteca.main.Adapters



import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import videoteca.main.MovieDetailsActivity
import videoteca.main.R
import videoteca.main.Domain.Movie.MovieResponse
import videoteca.main.Domain.Movie.PosterSize
import videoteca.main.Domain.MovieRentedInfo
import videoteca.main.MovieStreamActivity
import videoteca.main.api.TMDB_ImageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Adapter per gestire la visualizzazione dei film nollegiati.
 *
 * @param items Lista di informazioni sui film nollegiati da visualizzare.
 */

@GlideModule
class RentedMovieAdapter(private val items: List<MovieRentedInfo>) : RecyclerView.Adapter<RentedMovieAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "RentedAdapter"

    /**
     * Crea un nuovo ViewHolder quando non ci sono view esistenti che possono essere riutilizzate.
     *
     * @param parent Il ViewGroup a cui questa View verrà aggiunta dopo essere stata legata a un adattatore.
     * @param viewType Il tipo di vista della nuova View.
     * @return Un nuovo ViewHolder che contiene una View della lista di elementi.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_rentedmovies, parent, false)
        return ViewHolder(inflate)
    }

    /**
     * Associa i dati a un ViewHolder specifico in una posizione specifica nella lista.
     *
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto dell'elemento alla data posizione nella lista.
     * @param position La posizione dell'elemento nella lista degli elementi dell'adattatore.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = items[position].title
        Log.d(TAG, "tvTitle: ${holder.tvTitle.text}")
        var year:String = ""
        if(items[position].dateMovie!=""){
            year = items[position].dateMovie.substring(0, 4)
        }

        holder.tvYear.text = year
        Log.d(TAG, "tvYear: ${holder.tvYear.text}")

        val exdate = items[position].dateExpiration?.let { formatDate(it) }
        holder.tvExpiretionDate.text = exdate

        Log.d(TAG, "tvEx: ${holder.tvExpiretionDate.text}")

        var requestOptions = RequestOptions()
        val posterPath = TMDB_ImageManager().buildImageUrl(PosterSize.W342, items[position].posterPath)
        Log.d(TAG, "poster path = $posterPath")



        requestOptions = requestOptions
            .placeholder(R.drawable.ic_movie_default)
            .transform(CenterCrop(), RoundedCorners(30))
        Glide.with(context!!)
            .load(posterPath)
            .apply(requestOptions)
            .into(holder.poster)


        //mi reindirizza sul player del film per la riproduzione dei titolo
        holder.itemView.setOnClickListener { v: View? ->
            val intent = Intent(
                holder.itemView.context,
                MovieStreamActivity::class.java
            )
            intent.putExtra("id", items[position].id)
            context!!.startActivity(intent)
        }
    }

    /**
     * Restituisce il numero totale di elementi nella lista.
     *
     * @return Il numero totale di elementi nella lista.
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Formatta una data nel formato "dd/MM/yyyy HH:mm".
     *
     * @param date La data da formattare.
     * @return La data formattata come stringa.
     */
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    /**
     * ViewHolder interno che rappresenta la vista di un singolo elemento nella lista dei film affittati.
     *
     * @param itemView La vista dell'elemento.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvYear: TextView
        var poster: ImageView
        var tvExpiretionDate: TextView

        init {
            tvTitle = itemView.findViewById(R.id.tv_title_rented)
            tvYear = itemView.findViewById(R.id.tv_year_rented)
            poster = itemView.findViewById(R.id.iv_poster_rented)
            tvExpiretionDate = itemView.findViewById(R.id.tv_expirationDate_value)
        }
    }
}
