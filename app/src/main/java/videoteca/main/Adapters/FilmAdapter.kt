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
import videoteca.main.api.TMDB_ImageManager

/**
 * Adapter per visualizzare un elenco di film in un RecyclerView.
 *
 * @param items Lista di film da visualizzare.
 */
@GlideModule
class FilmAdapter(private val items: List<MovieResponse.Movie>) : RecyclerView.Adapter<FilmAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "FilmAdapter"

    /**
     * Crea un nuovo ViewHolder quando non ci sono viste esistenti che possono essere riutilizzate.
     *
     * @param parent Il ViewGroup nel quale la nuova View sarà aggiunta dopo essere stata associata a una posizione dell'adattatore.
     * @param viewType Il tipo di vista della nuova View.
     * @return Un nuovo ViewHolder che contiene una View del tipo di vista fornito.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_film, parent, false)
        return ViewHolder(inflate)
    }

    /**
     * Associa i dati di un determinato elemento alla posizione specificata nella RecyclerView.
     *
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto dell'elemento nella posizione data nel set di dati.
     * @param position La posizione dell'elemento nel set di dati dell'adattatore.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position].title
        var requestOptions = RequestOptions()
        val posterPath = TMDB_ImageManager().buildImageUrl(PosterSize.W342, items[position].posterPath)
        Log.d(TAG, "poster path = $posterPath")

        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(30))
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
     * Restituisce il numero totale di elementi nell'elenco dei film.
     *
     * @return Il numero totale di film nel set di dati.
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * ViewHolder per contenere e gestire le viste degli elementi dell'elenco dei film.
     *
     * @param itemView La vista radice che rappresenta un elemento dell'elenco.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var pic: ImageView

        init {
            textView = itemView.findViewById(R.id.tv_title)
            pic = itemView.findViewById(R.id.iv_poster)
        }
    }
}
