package com.example.myfestival.data

import com.example.myfestival.models.Dish

class MenuDataSet {

    val hotDogList: List<Dish> = createHotDogDataSet()
    val burgerList: List<Dish> = createBurgerDataSet()
    val pizzaList: List<Dish> = createPizzaDataSet()
    val frenchFriesList: List<Dish> = createFrenchFriesDataSet()
    val kebabList: List<Dish> = createKebabDataSet()

    fun createBurgerDataSet(): ArrayList<Dish> {
        val list = ArrayList<Dish>()

        list.add(
            Dish(
                "Classic Burger",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Cheeseburger",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Veggie Burger",
                2,
                true,
                false
            )
        )
        list.add(
            Dish(
                "Kingsize Burger",
                4,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Kingsize Cheeseburger",
                4,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Bacon Burger",
                3,
                false,
                false
            )
        )
        return list
    }

    fun createHotDogDataSet(): List<Dish> {
        val list = ArrayList<Dish>()

        list.add(
            Dish(
                "Hot dog",
                1,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Double hot dog",
                2,
                false,
                false
            )
        )
        return list
    }

    fun createPizzaDataSet(): List<Dish> {
        val list = ArrayList<Dish>()

        list.add(
            Dish(
                "Pizza margherita slice",
                1,
                true,
                false
            )
        )
        list.add(
            Dish(
                "Pizza margherita 30cm",
                6,
                true,
                false
            )
        )
        list.add(
            Dish(
                "Pizza hawaii slice",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                    "Pizza hawaii 30cm",
                    8,
                    false,
                    false
            )
        )
        list.add(
            Dish(
                "Pizza prosciutto slice",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Pizza prosciutto 30cm",
                8,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Veggie pizza slice",
                1,
                true,
                false
            )
        )
        list.add(
            Dish(
                "Veggie pizzza 30cm",
                6,
                true,
                false
            )
        )
        list.add(
            Dish(
                "Vegan pizza slice",
                1,
                true,
                true
            )
        )
        list.add(
            Dish(
                "Vegan pizza 30cm",
                6,
                true,
                true
            )
        )

        return list
    }

    fun createFrenchFriesDataSet(): List<Dish> {
        val list = ArrayList<Dish>()

        list.add(
            Dish(
                "Small fries",
                1,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Medium friets",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Large fries",
                3,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Bicky Burger",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Bicky Cheese",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Bitterballen",
                1,
                false,
                false
            )
        )
        list.add(
            Dish(
                "2x frikandel",
                1,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Mexicano",
                2,
                false,
                false
            )
        )
        return list
    }

    fun createKebabDataSet(): List<Dish> {
        val list = ArrayList<Dish>()

        list.add(
            Dish(
                "Small pita",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Large pita",
                3,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Small pita chicken",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Large pita chicken",
                3,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Small pita lamb",
                2,
                false,
                false
            )
        )
        list.add(
            Dish(
                "Large pita lamb",
                3,
                false,
                false
            )
        )
    }
}
