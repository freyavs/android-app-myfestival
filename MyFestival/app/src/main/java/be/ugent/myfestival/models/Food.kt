package be.ugent.myfestival.models

import com.google.firebase.storage.StorageReference

data class FoodStand(val id: String, val name: String, val logoRef: StorageReference, val menu: List<Dish>)

data class Dish(val dish: String, val price: String, val veggie: Boolean, val vegan: Boolean){
    //nodig voor list adapter maar zet ze appart want dan kan firebase makkelijk dish inladen
    var id = ""
    //nodig voor firebase
    constructor() : this("", "", false, false)
}