package com.example.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfestival.R
import com.example.myfestival.models.FoodStand
import kotlinx.android.synthetic.main.foodstand_item.view.*

class FoodStandAdapter() :
    RecyclerView.Adapter<FoodStandAdapter.FoodStandViewHolder>() {

    var foodStandList = emptyList<FoodStand>()

    class FoodStandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.fastfood_image_view
        val textView1: TextView = itemView.fastfood_name_text_view

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodStandViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.foodstand_item, parent, false)

        return FoodStandViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: FoodStandViewHolder, position: Int) {
        val currentItem = foodStandList[position]

        holder.imageView.setImageResource(currentItem.foodstandImg)
        holder.textView1.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return foodStandList.size
    }
}