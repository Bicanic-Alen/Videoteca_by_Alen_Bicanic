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
import videoteca.main.gestioneAPI.TMDB_ImageManager

@GlideModule
class FilmAdapter(private val items: List<MovieResponse.Movie>) : RecyclerView.Adapter<FilmAdapter.ViewHolder>() {

    private var context: Context? = null
    private val TAG = "FilmAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_film, parent, false)
        return ViewHolder(inflate)
    }

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

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var pic: ImageView

        init {
            textView = itemView.findViewById(R.id.tv_title)
            pic = itemView.findViewById(R.id.iv_poster)
        }
    }
}
