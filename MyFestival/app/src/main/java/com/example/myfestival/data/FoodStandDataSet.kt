package com.example.myfestival.data

import com.example.myfestival.R
import com.example.myfestival.models.FoodStand


// dit is de hardcoded data voor de foodstands
class FoodStandDataSet {

        companion object{
            fun createDataSet(): ArrayList<FoodStand> {
                val list = ArrayList<FoodStand>()
                list.add(
                    FoodStand(
                        "Hotter Dogs",
                        R.drawable.ic_fastfood
                    )
                )
                list.add(
                    FoodStand(
                        "Burger Boy's",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Pizza Town",
                        R.drawable.ic_pizza                    )
                )
                list.add(
                    FoodStand(
                        "Donny's Burgers",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Pizza For You",
                        R.drawable.ic_pizza                    )
                )
                list.add(
                    FoodStand(
                        "Frietjes bij Pol",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "French Fries",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Pizza Lovers",
                        R.drawable.ic_pizza                    )
                )
                list.add(
                    FoodStand(
                        "Kebab 'n' Burgers",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Kebab Shop",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Friet Shop",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Burger Shop",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Hot Dog Shop",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Pizza Shop",
                        R.drawable.ic_pizza                    )
                )
                return list
            }
        }
}