package be.ugent.myfestival.models

data class FoodStand(val id: String, val name: String, val foodstandImg: Int)

data class Dish(val name: String, val price: Int, val vegetarian: Boolean, val vegan: Boolean)