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

@GlideModule
class SearchMovieAdapter(private val items: List<MovieResponse.Movie>) : RecyclerView.Adapter<SearchMovieAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "FilmAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_searchitem, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "nome film: ${items[position].title}, id film: ${items[position].id}")
        holder.tvTitle.text = items[position].title
        var year:String = ""
        if(items[position].releaseDate!=""){
            year = items[position].releaseDate.substring(0, 4)
        }
        holder.tvYear.text = year
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


        holder.itemView.setOnClickListener { v: View? ->
            val intent = Intent(
                holder.itemView.context,
                MovieDetailsActivity::class.java
            )
            intent.putExtra("id", items[position].id)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvYear: TextView
        var poster: ImageView

        init {
            tvTitle = itemView.findViewById(R.id.tv_title_search)
            tvYear = itemView.findViewById(R.id.tv_year_search)
            poster = itemView.findViewById(R.id.iv_poster_search)
        }
    }
}
