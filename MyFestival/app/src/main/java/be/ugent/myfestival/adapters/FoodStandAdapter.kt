package be.ugent.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R
import be.ugent.myfestival.models.FoodStand
import kotlinx.android.synthetic.main.foodstand_item.view.*


class FoodStandAdapter(val clickListener: (FoodStand) -> Unit) :
    RecyclerView.Adapter<FoodStandAdapter.FoodStandViewHolder>() {

    var foodStandList = emptyList<FoodStand>()

    class FoodStandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.foodstand_image_view
        val textView1: TextView = itemView.fastfood_name_text_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodStandViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.foodstand_item, parent, false)

        return FoodStandViewHolder(
            itemView
        )
    }


    override fun onBindViewHolder(holder: FoodStandViewHolder, position: Int) {
        val currentItem = foodStandList[position]

        holder.imageView.setImageResource(currentItem.logo)
        holder.textView1.text = currentItem.name
        holder.itemView.setOnClickListener{clickListener(currentItem)}
    }

    override fun getItemCount(): Int {
        return foodStandList.size
    }
}