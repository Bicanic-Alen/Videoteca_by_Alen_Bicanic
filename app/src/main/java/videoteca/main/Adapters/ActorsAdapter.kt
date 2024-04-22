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


@GlideModule
class ActorsAdapter (private val items: List<CreditsMovie.Cast>): RecyclerView.Adapter<ActorsAdapter.ViewHolder>() {
    private var context: Context? = null
    private val TAG = "ActorsAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsAdapter.ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_actors, parent, false)
        return ViewHolder(inflate)
    }

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


        /*
        holder.itemView.setOnClickListener { v: View? ->
            val intent = Intent(
                holder.itemView.context,
                MovieDetailsActivity::class.java
            )
            intent.putExtra("id", items[position].id)
            context!!.startActivity(intent)
        }*/
    }

    override fun getItemCount(): Int {
        return items.size
    }





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