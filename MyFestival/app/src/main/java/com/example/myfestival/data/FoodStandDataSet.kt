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
                        "meat",
                        R.drawable.ic_fastfood
                    )
                )
                list.add(
                    FoodStand(
                        "Burger Boy's",
                        "meat, vegetarian",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Pizza Town",
                        "meat, vegetarian, vegan",
                        R.drawable.ic_pizza                    )
                )
                list.add(
                    FoodStand(
                        "Donny's Burgers",
                        "meat",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Pizza For You",
                        "meat, vegetarian",
                        R.drawable.ic_pizza                    )
                )
                list.add(
                    FoodStand(
                        "Frietjes bij Pol",
                        "meat, vegetarian, vegan",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "French Fries",
                        "meat, vegetarian",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Pizza Lovers",
                        "meat, vegetarian",
                        R.drawable.ic_pizza                    )
                )
                list.add(
                    FoodStand(
                        "Kebab 'n' Burgers",
                        "meat",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Kebab Shop",
                        "meat",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Friet Shop",
                        "meat, vegetarian, vegan",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Burger Shop",
                        "meat",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Hot Dog Shop",
                        "meat",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Pizza Shop",
                        "meat, vegetarian, vegan",
                        R.drawable.ic_pizza                    )
                )
                return list
            }
        }
}