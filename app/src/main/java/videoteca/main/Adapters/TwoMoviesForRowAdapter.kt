package videoteca.main.Adapters



import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
class TwoMoviesForRowAdapter(private val items: List<MovieResponse.Movie>) : RecyclerView.Adapter<TwoMoviesForRowAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "FilmAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_twomoviesforrow, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val firstIndex = position * 2
        val secondIndex = firstIndex + 1

        if (firstIndex < items.size) {
            val firstMovie = items[firstIndex]
            holder.bind(firstMovie, holder.lyt1)
        } else {
            // Nascondi il primo layout se non esiste
            holder.lyt1.visibility = View.INVISIBLE
        }


        if (secondIndex < items.size) {
            val secondMovie = items[secondIndex]
            holder.bind(secondMovie, holder.lyt2)
        } else {

            holder.lyt2.visibility = View.INVISIBLE
        }
    }


    private fun loadPoster(posterPath: String?, imageView: ImageView) {
        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(30))
        val posterUrl = posterPath?.let { TMDB_ImageManager().buildImageUrl(PosterSize.W342, it) }
        Glide.with(context!!)
            .load(posterUrl)
            .apply(requestOptions)
            .into(imageView)
    }


    override fun getItemCount(): Int {

        return (items.size + 1) / 2
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lyt1: ConstraintLayout = itemView.findViewById(R.id.lyt1)
        val lyt2: ConstraintLayout = itemView.findViewById(R.id.lyt2)

        fun bind(movie: MovieResponse.Movie, layout: ConstraintLayout) {
            layout.visibility = View.VISIBLE
            val textView: TextView = layout.findViewById(R.id.tv_title)
            val posterImageView: ImageView = layout.findViewById(R.id.iv_poster)

            // Imposta i dati del film nel layout
            textView.text = movie.title
            loadPoster(movie.posterPath, posterImageView)

            // Gestisci il click sul layout del film
            layout.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id", movie.id)
                context?.startActivity(intent)
            }
        }
    }
}
