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


class GenreAdapterMovieDetails(private val items: List<Genre>): RecyclerView.Adapter<GenreAdapterMovieDetails.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "GenreAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapterMovieDetails.ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_genresdetails, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: GenreAdapterMovieDetails.ViewHolder, position: Int) {
        holder.textView.text = items.get(position).name

        holder.itemView.setOnClickListener { v: View? ->
            val intent = Intent(
                holder.itemView.context,
                GenresActivity::class.java
            )
            intent.putExtra("id", items.get(position).id)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO
        val textView: TextView
        init {
            textView = itemView.findViewById(R.id.tv_genresname_details)
        }
    }


}