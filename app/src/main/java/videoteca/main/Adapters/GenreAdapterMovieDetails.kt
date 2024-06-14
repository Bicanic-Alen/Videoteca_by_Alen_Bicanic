package videoteca.main.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import videoteca.main.Domain.Movie.Genre
import videoteca.main.GenresActivity

import videoteca.main.R

/**
 * Adapter per gestire la visualizzazione dei generi di un film nei dettagli del film.
 *
 * @param items Lista di generi da visualizzare.
 */
class GenreAdapterMovieDetails(private val items: List<Genre>): RecyclerView.Adapter<GenreAdapterMovieDetails.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "GenreAdapter"

    /**
     * Crea un nuovo ViewHolder quando non ci sono view esistenti che possono essere riutilizzate.
     *
     * @param parent Il ViewGroup a cui questa View verrà aggiunta dopo essere stata legata a un adattatore.
     * @param viewType Il tipo di vista della nuova View.
     * @return Un nuovo ViewHolder che contiene una View della lista di elementi.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapterMovieDetails.ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_genresdetails, parent, false)
        return ViewHolder(inflate)
    }

    /**
     * Associa i dati a un ViewHolder specifico in una posizione specifica nella lista.
     *
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto dell'elemento alla data posizione nella lista.
     * @param position La posizione dell'elemento nella lista degli elementi dell'adattatore.
     */
    override fun onBindViewHolder(holder: GenreAdapterMovieDetails.ViewHolder, position: Int) {
        holder.textView.text = items.get(position).name

        //premenedo sul genere in questione apro la pagina, con i film di quel genere
        holder.itemView.setOnClickListener { v: View? ->
            val intent = Intent(
                holder.itemView.context,
                GenresActivity::class.java
            )
            intent.putExtra("id", items.get(position).id)
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
     * ViewHolder interno che rappresenta la vista di un singolo elemento nella lista dei generi del film.
     *
     * @param itemView La vista dell'elemento.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView
        init {
            textView = itemView.findViewById(R.id.tv_genresname_details)
        }
    }


}