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

class SliderAdapters(movieResponse: MovieResponse, viewPager2: ViewPager2) :
    RecyclerView.Adapter<SliderAdapters.SliderViewHolder>() {

    private val movieList: MutableList<MovieResponse.Movie>
    private val viewPager2: ViewPager2
    private var context: Context? = null

    init {
        movieList = movieResponse.results.toMutableList()
        this.viewPager2 = viewPager2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        context = parent.context
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_conteiner, parent, false
            )
        )
    }

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

    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.imageSlide)
        }

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

    private val runnable = Runnable {
        movieList.addAll(movieList)
        notifyDataSetChanged()
    }

}
