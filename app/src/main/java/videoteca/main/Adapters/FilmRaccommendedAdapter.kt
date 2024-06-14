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
import videoteca.main.Domain.Movie.MovieResponseRecommended
import videoteca.main.Domain.Movie.PosterSize
import videoteca.main.api.TMDB_ImageManager

/**
 * Adapter per visualizzare film consigliati in un RecyclerView.
 *
 * @param items Lista di film consigliati da visualizzare.
 */
@GlideModule
class FilmRaccommendedAdapter(private val items: List<MovieResponseRecommended.MovieRaccomended>) : RecyclerView.Adapter<FilmRaccommendedAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "FilmAdapterRecommended"

    /**
     * Crea un nuovo ViewHolder quando non ci sono viste esistenti che possono essere riutilizzate.
     *
     * @param parent Il ViewGroup in cui verrà aggiunta la nuova View dopo essere stata associata a una posizione dell'adattatore.
     * @param viewType Il tipo di vista della nuova View.
     * @return Un nuovo ViewHolder che contiene una View del tipo di vista specificato.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_recommendedfilm, parent, false)
        return ViewHolder(inflate)
    }

    /**
     * Collega i dati a un ViewHolder specifico in una posizione specifica della lista.
     *
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto dell'elemento nella posizione specificata nel set di dati.
     * @param position La posizione dell'elemento nel set di dati dell'adattatore.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvTitle.text = items[position].title

        var requestOptions = RequestOptions()
        val posterPath =
            items[position].posterPath?.let {
                TMDB_ImageManager().buildImageUrl(
                    PosterSize.W342,
                    it
                )
            }
        Log.d(TAG, "poster path = $posterPath")

        requestOptions = requestOptions
            .placeholder(R.drawable.ic_movie_default)
            .transform(CenterCrop(), RoundedCorners(30))


        Glide.with(context!!)
            .load(posterPath)
            .apply(requestOptions)
            .into(holder.pic)


        holder.itemView.setOnClickListener { v: View? ->
            val intent = Intent(
                holder.itemView.context,
                MovieDetailsActivity::class.java
            )
            intent.putExtra("id", items[position].id)
            context!!.startActivity(intent)
        }
    }

    /**
     * Restituisce il numero totale di elementi nel set di dati detenuto dall'adattatore.
     *
     * @return Il numero totale di elementi nel set di dati.
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Classe ViewHolder che rappresenta la vista di un singolo elemento nella lista di film consigliati.
     *
     * @param itemView La vista dell'elemento.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var pic: ImageView
        init {
            tvTitle = itemView.findViewById(R.id.tv_title_rec)
            pic = itemView.findViewById(R.id.iv_poster_recommended)
        }
    }
}
