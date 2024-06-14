package videoteca.main.Adapters

import android.content.Context
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
import videoteca.main.R
import videoteca.main.Domain.Movie.CreditsMovie
import videoteca.main.Domain.Movie.ProfileSize
import videoteca.main.api.TMDB_ImageManager

/**
 * Adapter per gestire l'elenco degli attori di un film.
 *
 * @param items Lista dei dati sugli attori da visualizzare.
 */
@GlideModule
class ActorsAdapter (private val items: List<CreditsMovie.Cast>): RecyclerView.Adapter<ActorsAdapter.ViewHolder>() {
    private var context: Context? = null
    private val TAG = "ActorsAdapter"

    /**
     * Crea un nuovo ViewHolder quando non ci sono viste esistenti che possono essere riutilizzate.
     *
     * @param parent Il ViewGroup nel quale la nuova View sarà aggiunta dopo essere stata associata a una posizione dell'adattatore.
     * @param viewType Il tipo di vista della nuova View.
     * @return Un nuovo ViewHolder che contiene una View del tipo di vista fornito.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsAdapter.ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_actors, parent, false)
        return ViewHolder(inflate)
    }

    /**
     * Associa i dati di un determinato elemento alla posizione specificata nella RecyclerView.
     *
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto dell'elemento nella posizione data nel set di dati.
     * @param position La posizione dell'elemento nel set di dati dell'adattatore.
     */
    override fun onBindViewHolder(holder: ActorsAdapter.ViewHolder, position: Int) {
        holder.tvActorname.text = items.get(position).name
        holder.tvCharacterName.text = items.get(position).character
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_person_notfound) // Imposta l'immagine predefinita
            .transform(CenterCrop(), RoundedCorners(30))

        val profilePath = items[position].profilePath
        val imageUrl = profilePath?.let { TMDB_ImageManager().buildImageUrl(ProfileSize.W185, it) }

        Glide.with(context!!)
            .load(imageUrl)
            .apply(requestOptions)
            .into(holder.pic)
    }

    /**
     * Restituisce il numero totale di elementi nell'elenco degli attori.
     *
     * @return Il numero totale di attori nel set di dati.
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * ViewHolder per contenere e gestire le viste degli elementi dell'elenco degli attori.
     *
     * @param itemView La vista radice che rappresenta un elemento dell'elenco.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvActorname: TextView
        var tvCharacterName: TextView
        var pic: ImageView

        init {
            tvActorname = itemView.findViewById(R.id.tv_actor_name)
            tvCharacterName = itemView.findViewById(R.id.tv_character_name)
            pic = itemView.findViewById(R.id.iv_actor)
        }
    }

}