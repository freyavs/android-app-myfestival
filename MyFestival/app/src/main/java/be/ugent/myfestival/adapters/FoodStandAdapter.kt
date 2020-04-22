package be.ugent.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R
import be.ugent.myfestival.models.FoodStand
import be.ugent.myfestival.utilities.GlideApp
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.foodstand_item.view.*

class FoodStandAdapter(val clickListener: (FoodStand) -> Unit) :
    ListAdapter<FoodStand, RecyclerView.ViewHolder>(FoodStandItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodStandViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.foodstand_item, parent, false)

        return FoodStandViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position )

        GlideApp.with((holder as FoodStandViewHolder).imageView.context)
                .load(currentItem.logo)
                .into(holder.imageView)

        holder.textView1.text = currentItem.name
        holder.itemView.setOnClickListener{clickListener(currentItem)}
    }


    class FoodStandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.foodstand_image_view
        val textView1: TextView = itemView.fastfood_name_text_view
    }
}

private class FoodStandItemDiffCallback : DiffUtil.ItemCallback<FoodStand>() {

    override fun areItemsTheSame(oldItem: FoodStand, newItem: FoodStand): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: FoodStand, newItem: FoodStand): Boolean {
        return oldItem.name == newItem.name
    }
}