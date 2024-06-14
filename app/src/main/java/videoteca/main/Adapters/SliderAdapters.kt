package videoteca.main.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import videoteca.main.Domain.Movie.ImageSize
import videoteca.main.Domain.Movie.MovieResponse
import videoteca.main.MovieDetailsActivity
import videoteca.main.R
import videoteca.main.api.TMDB_ImageManager

/**
 * Adapter per gestire la visualizzazione dei film in uno slider (ViewPager2).
 *
 * @param movieResponse La risposta del film contenente la lista dei film.
 * @param viewPager2 L'istanza di ViewPager2 che utilizza questo adapter.
 */

class SliderAdapters(movieResponse: MovieResponse, viewPager2: ViewPager2) :
    RecyclerView.Adapter<SliderAdapters.SliderViewHolder>() {

    private val movieList: MutableList<MovieResponse.Movie>
    private val viewPager2: ViewPager2
    private var context: Context? = null


    init {
        movieList = movieResponse.results.toMutableList()
        this.viewPager2 = viewPager2
    }

    /**
     * Crea un nuovo ViewHolder quando non ci sono view esistenti che possono essere riutilizzate.
     *
     * @param parent Il ViewGroup a cui questa View verrà aggiunta dopo essere stata legata a un adattatore.
     * @param viewType Il tipo di vista della nuova View.
     * @return Un nuovo ViewHolder che contiene una View della lista di elementi.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        context = parent.context
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_conteiner, parent, false
            )
        )
    }

    /**
     * Associa i dati a un ViewHolder specifico in una posizione specifica nella lista.
     *
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto dell'elemento alla data posizione nella lista.
     * @param position La posizione dell'elemento nella lista degli elementi dell'adattatore.
     */
    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {

        holder.setImage(movieList[position].backdropPath)
        if (position == movieList.size - 2) {
            viewPager2.post(runnable)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                MovieDetailsActivity::class.java
            )
            intent.putExtra("id",movieList[position].id)
            context!!.startActivity(intent)
        }

    }

    /**
     * Restituisce il numero totale di elementi nella lista.
     *
     * @return Il numero totale di elementi nella lista.
     */
    override fun getItemCount(): Int {
        return movieList.size
    }

    /**
     * ViewHolder interno che rappresenta la vista di un singolo elemento nello slider.
     *
     * @param itemView La vista dell'elemento.
     */
    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.imageSlide)
        }

        /**
         * Imposta l'immagine del film nello slider.
         *
         * @param imageUrl L'URL dell'immagine del film.
         */
        fun setImage(imageUrl: String) {

            val imagepath = TMDB_ImageManager().buildImageUrl(ImageSize.W780,imageUrl)

            val requestOptions = RequestOptions()
                .transform(CenterCrop(), RoundedCorners(60))
            Glide.with(context!!)
                .load(imagepath)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    /**
     * Runnable che duplica la lista dei film quando raggiunge la fine.
     */
    private val runnable = Runnable {
        movieList.addAll(movieList)
        notifyDataSetChanged()
    }

}
