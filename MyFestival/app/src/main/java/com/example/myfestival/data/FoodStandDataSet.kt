package com.example.myfestival.data

import com.example.myfestival.R
import com.example.myfestival.models.FoodStand


// dit is de hardcoded data voor de foodstands
class FoodStandDataSet {

        companion object{
            fun createDataSet(): ArrayList<FoodStand> {
                val menus = MenuDataSet()
                val list = ArrayList<FoodStand>()
                list.add(
                    FoodStand(
                        "Hotter Dogs",
                        R.drawable.ic_fastfood,
                        menus.hotDogList
                    )
                )
                list.add(
                    FoodStand(
                        "Burger Boy's",
                        R.drawable.ic_fastfood,
                        menus.burgerList
                    )
                )
                list.add(
                    FoodStand(
                        "Pizza Town",
                        R.drawable.ic_pizza,
                        menus.pizzaList
                    )
                )
                list.add(
                    FoodStand(
                        "Donny's Burgers",
                        R.drawable.ic_fastfood,
                        menus.burgerList
                    )
                )
                list.add(
                    FoodStand(
                        "Pizza For You",
                        R.drawable.ic_pizza,
                        menus.pizzaList
                    )
                )
                list.add(
                    FoodStand(
                        "Frietjes bij Pol",
                        R.drawable.ic_fastfood,
                        menus.frenchFriesList
                    )
                )
                list.add(
                    FoodStand(
                        "French Fries",
                        R.drawable.ic_fastfood,
                        menus.frenchFriesList
                    )
                )
                list.add(
                    FoodStand(
                        "Pizza Lovers",
                        R.drawable.ic_pizza,
                        menus.pizzaList
                    )
                )
                list.add(
                    FoodStand(
                        "Kebab Shop",
                        R.drawable.ic_fastfood,
                        menus.kebabList
                    )
                )
                list.add(
                    FoodStand(
                        "Friet Shop",
                        R.drawable.ic_fastfood                    )
                )
                list.add(
                    FoodStand(
                        "Burger Shop",
                        R.drawable.ic_fastfood,
                        menus.burgerList
                    )
                )
                list.add(
                    FoodStand(
                        "Hot Dog Shop",
                        R.drawable.ic_fastfood,
                        menus.hotDogList
                    )
                )
                list.add(
                    FoodStand(
                        "Pizza Shop",
                        R.drawable.ic_pizza,
                        menus.pizzaList
                    )
                )
                return list
            }
        }
}