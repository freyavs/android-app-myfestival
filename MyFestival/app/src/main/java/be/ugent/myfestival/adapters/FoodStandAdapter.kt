package be.ugent.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R
import be.ugent.myfestival.models.FoodStand
import be.ugent.myfestival.utilities.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.list_item.view.*

class FoodStandAdapter(val clickListener: (FoodStand) -> Unit) : RecyclerView.Adapter<FoodStandAdapter.FoodStandViewHolder>() {
    /*
    Deze adapter lijkt sterk op de FestivalChooserAdapter maar we gebruiken een andere clickListener,
    ook houden we graag overzicht door deze 2 adapters gesplitst te houden.
     */

    var foodstands = emptyList<FoodStand>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodStandViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return FoodStandViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodStandViewHolder, position: Int) {
        val currentItem = foodstands[position]

        GlideApp.with(holder.imageView.context)
                .load(currentItem.logoRef)
                .placeholder(R.drawable.no_internet)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.imageView)

        holder.textView.text = currentItem.name
        holder.itemView.setOnClickListener{clickListener(currentItem)}
    }

    override fun getItemCount(): Int {
        return foodstands.size
    }

    class FoodStandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.list_item_image_view
        val textView: TextView = itemView.list_item_text_view
    }
}
