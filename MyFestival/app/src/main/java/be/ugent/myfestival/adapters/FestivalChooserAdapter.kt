package be.ugent.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.utilities.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.festival_chooser_item.view.*

class FestivalChooserAdapter(val clickListener: (FestivalChooser) -> Unit) :
    RecyclerView.Adapter<FestivalChooserAdapter.FestivalChooserViewHolder>(){

    var festivalList = emptyList<FestivalChooser>()

    class FestivalChooserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.festival_name
        val imageView: ImageView = itemView.festival_logo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalChooserViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.festival_chooser_item,parent,false)
        return FestivalChooserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FestivalChooserViewHolder, position: Int) {
        val currentItem = festivalList[position]

        GlideApp.with(holder.imageView.context)
            .load(currentItem.logoRef)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(holder.imageView)

        holder.textView.text = currentItem.name
        holder.itemView.setOnClickListener{clickListener(currentItem)}
    }

    override fun getItemCount(): Int {
        return festivalList.size
    }
}