package be.ugent.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R
import be.ugent.myfestival.models.NewsfeedItem
import be.ugent.myfestival.utilities.GlideApp
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import kotlinx.android.synthetic.main.newsfeed_item.view.*

class NewsfeedAdapter(val viewModel : FestivalViewModel) : RecyclerView.Adapter<NewsfeedAdapter.NewsItemViewHolder>(){

    var posts = emptyList<NewsfeedItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.newsfeed_item, parent, false)

        return NewsItemViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val post = posts[position]

        holder.title.text = post.title
        holder.time.text = post.time
        holder.message.text = post.message
        if (post.image != null) {
            GlideApp.with(holder.image.context)
                .load(post.image)
                .into(holder.image)
        }

      /*  GlideApp.with(holder.logo.context)
            .load(viewModel.getLogo().value)
            .into(holder.logo)
        */
    }

    override fun getItemCount() = posts.size

    class NewsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.organiser
        val time: TextView = itemView.time
        val message: TextView = itemView.message
        val image: ImageView = itemView.image
        val logo: ImageView = itemView.image
    }
}