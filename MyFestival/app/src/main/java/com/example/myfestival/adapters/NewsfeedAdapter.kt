package com.example.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfestival.R
import com.example.myfestival.models.NewsfeedItem
import kotlinx.android.synthetic.main.newsfeed_item.view.*

class NewsfeedAdapter() : RecyclerView.Adapter<NewsfeedAdapter.NewsItemViewHolder>(){

    var posts = emptyList<NewsfeedItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.newsfeed_item, parent, false)

        return NewsItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val post = posts[position]

        holder.organiser.text = post.organiser
        //holder.time.text = post.time
        holder.time.text = "${(10..23).shuffled().first()}:${(10..60).shuffled().first()}"
        holder.message.text = post.message
        holder.logo.setImageResource(post.logo)
        holder.image.setImageResource(post.image)
    }

    override fun getItemCount() = posts.size

    class NewsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val organiser: TextView = itemView.organiser
        val time: TextView = itemView.time
        val message: TextView = itemView.message
        val image: ImageView = itemView.image
        val logo: ImageView = itemView.image
    }
}