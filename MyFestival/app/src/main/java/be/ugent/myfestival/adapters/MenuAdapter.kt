package be.ugent.myfestival.adapters

import be.ugent.myfestival.models.Dish
import kotlinx.android.synthetic.main.menu_item.view.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R

class MenuAdapter() : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    var menuList = emptyList<Dish>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.menu_item, parent, false)

        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = menuList[position]

        holder.dish.text = currentItem.dish
        holder.price.text = currentItem.price

        if (currentItem.vegan && currentItem.veggie) {  holder.diet.text = "vegetarisch, veganistisch" }
        else if (currentItem.veggie) {  holder.diet.text = "vegetarisch" }
        else if (currentItem.vegan) {  holder.diet.text = "veganistisch" }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dish: TextView = itemView.menu_dish_name_text_view
        val diet: TextView = itemView.menu_dish_diets_text_view
        val price: TextView = itemView.menu_dish_price_text_view

    }
}