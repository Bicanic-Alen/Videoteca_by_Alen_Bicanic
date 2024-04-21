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


class GenreAdapter(private val items: GenreList): RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "GenreAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_genres, parent, false)
        return ViewHolder(inflate)
    }

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

    override fun getItemCount(): Int {
        return items.genres.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO
        val textView: TextView
        init {
            textView = itemView.findViewById(R.id.tv_genrename)
        }
    }


}