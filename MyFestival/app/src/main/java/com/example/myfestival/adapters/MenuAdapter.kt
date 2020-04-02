package com.example.myfestival.adapters

import com.example.myfestival.models.Dish
import kotlinx.android.synthetic.main.menu_item.view.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfestival.R

class MenuAdapter(val menuList: List<Dish>) :
        RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1 = itemView.menu_dish_name_text_view
        val textView2 = itemView.menu_dish_diets_text_view
        val textView3 = itemView.menu_dish_price_text_view

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.menu_item, parent, false)

        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = menuList[position]

        holder.textView1.text = currentItem.name

        var dietString = ""
        if (currentItem.vegan && currentItem.vegetarian) { dietString = "vegetarian, vegan" }
        else if (currentItem.vegetarian) { dietString = "vegetarian" }
        else if (currentItem.vegan) { dietString = "vegan" }
        holder.textView2.text = dietString

        val priceString: String = currentItem.price.toString()
        holder.textView3.text = priceString
    }

    override fun getItemCount(): Int {
        return menuList.size
    }
}