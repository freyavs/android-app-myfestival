package be.ugent.myfestival.models

data class FoodStand(val id: String, val name: String, val logo: String, val menu: List<Dish>)

data class Dish(val dish: String, val price: String, val veggie: Boolean, val vegan: Boolean){
    //nodig voor firebase
    constructor() : this("", "", false, false)
}