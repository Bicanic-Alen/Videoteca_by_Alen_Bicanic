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
 * Adapter per visualizzare i primi generi preferiti al momento della registrazione in un RecyclerView.
 *
 * @param items Oggetto GenreList contenente la lista di generi da visualizzare.
 */
class FirstFavoriteGenreAdapter(private val items: GenreList): RecyclerView.Adapter<FirstFavoriteGenreAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "GenreAdapter"

    /**
     * Crea un nuovo ViewHolder quando non ci sono viste esistenti che possono essere riutilizzate.
     *
     * @param parent Il ViewGroup in cui verrà aggiunta la nuova View dopo essere stata associata a una posizione dell'adattatore.
     * @param viewType Il tipo di vista della nuova View.
     * @return Un nuovo ViewHolder che contiene una View del tipo di vista specificato.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstFavoriteGenreAdapter.ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_genres, parent, false)
        return ViewHolder(inflate)
    }

    /**
     * Collega i dati a un ViewHolder specifico in una posizione specifica della lista.
     *
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto dell'elemento nella posizione specificata nel set di dati.
     * @param position La posizione dell'elemento nel set di dati dell'adattatore.
     */
    override fun onBindViewHolder(holder: FirstFavoriteGenreAdapter.ViewHolder, position: Int) {
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
     * Restituisce il numero totale di elementi nel set di dati detenuto dall'adattatore.
     *
     * @return Il numero totale di elementi nel set di dati.
     */
    override fun getItemCount(): Int {
        return items.genres.size
    }

    /**
     * Classe ViewHolder che rappresenta la vista di un singolo elemento nella lista di generi preferiti.
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