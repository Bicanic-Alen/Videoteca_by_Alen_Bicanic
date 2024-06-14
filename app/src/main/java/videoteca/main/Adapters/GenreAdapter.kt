package videoteca.main.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import videoteca.main.Domain.GenreList
import videoteca.main.GenresActivity

import videoteca.main.R


/**
 * Adapter per gestire la visualizzazione dei generi di film.
 *
 * @param items Oggetto GenreList contenente la lista dei generi da visualizzare.
 */
class GenreAdapter(private val items: GenreList): RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "GenreAdapter"

    /**
     * Crea un nuovo ViewHolder quando non ci sono view esistenti che possono essere riutilizzate.
     *
     * @param parent Il ViewGroup a cui questa View verrà aggiunta dopo essere stata legata a un adattatore.
     * @param viewType Il tipo di vista della nuova View.
     * @return Un nuovo ViewHolder che contiene una View della lista di elementi.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_genres, parent, false)
        return ViewHolder(inflate)
    }

    /**
     * Associa i dati a un ViewHolder specifico in una posizione specifica nella lista.
     *
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto dell'elemento alla data posizione nella lista.
     * @param position La posizione dell'elemento nella lista degli elementi dell'adattatore.
     */
    override fun onBindViewHolder(holder: GenreAdapter.ViewHolder, position: Int) {
        holder.textView.text = items.genres.get(position).name


        holder.itemView.setOnClickListener { v: View? ->
           val intent = Intent(
                holder.itemView.context,
                GenresActivity::class.java
            )
            intent.putExtra("id", items.genres.get(position).id)
            context!!.startActivity(intent)
        }
    }

    /**
     * Restituisce il numero totale di elementi nella lista.
     *
     * @return Il numero totale di elementi nella lista.
     */
    override fun getItemCount(): Int {
        return items.genres.size
    }

    /**
     * ViewHolder interno che rappresenta la vista di un singolo elemento nella lista dei generi di film.
     *
     * @param itemView La vista dell'elemento.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO
        val textView: TextView
        init {
            textView = itemView.findViewById(R.id.tv_genrename)
        }
    }


}