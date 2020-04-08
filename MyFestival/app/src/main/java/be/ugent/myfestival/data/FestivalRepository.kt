package be.ugent.myfestival.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import be.ugent.myfestival.models.*
import be.ugent.myfestival.R
import be.ugent.myfestival.models.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import be.ugent.myfestival.models.*


class FestivalRepository(val database: FirebaseDatabase) {
    var name: MutableLiveData<String> = MutableLiveData()

    val TAG = "FIREBASEtag"

    //TODO: deadline 2 - festivalID kunnen kiezen
    val festivalID = "-M3b9hJNsFaCXAi8Gegq"

    //voor debug redenen:
    val connectedRef = Firebase.database.getReference(".info/connected")
    fun addConnectionListener() {
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d(TAG, "connected")
                } else {
                    Log.d(TAG, "not connected")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listener was cancelled")
            }
        })
    }

    // ------------- data voor het home menu -------------------------------------
    fun getFestivalName(): MutableLiveData<String> {
        if (name.value == null) {
            //TODO: debug info weg
            addConnectionListener()
            Log.d(TAG, "Adding listener to name")
            FirebaseDatabase.getInstance()
                .getReference(festivalID).child("name")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            name.postValue(dataSnapshot.value.toString())
                            Log.d(TAG, "getName:onDataChange -> name exists")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "getName:onCancelled", databaseError.toException())
                    }
                })
        }
        return name
    }


    // -----------------------------  (hardcoded) data voor de lineup --------------------------------
    fun getLineup() : MutableLiveData<List<LineupDay>> {
        val concertsMain = listOf(
            Concert(
                "Json Derulo",
                "18:30",
                "20:00"
            ),
            Concert(
                "Zwangere Guy",
                "20:30",
                "22:00"
            )
        )
        val concertsForest = listOf(
            Concert(
                "Frank Ocean",
                "18:30",
                "20:00"
            ),
            Concert(
                "George Bucks",
                "20:30",
                "22:00"
            )
        )
        val concertsHerbakker = listOf(
            Concert(
                "Eddy de Ketelaeare",
                "18:30",
                "22:00"
            )
        )

        val Mainstage =
            Stage("Mainstage", concertsMain)
        val Forestage =
            Stage("Foreststage", concertsForest)
        val Herbakkerstage = Stage(
            "Herbakkerstage",
            concertsHerbakker
        )

        val stages = listOf(Mainstage, Forestage)

        val day1 = LineupDay("Donderdag", stages)
        val day2 = LineupDay(
            "Vrijdag",
            listOf(Herbakkerstage)
        )

        return MutableLiveData(listOf(day1, day2))
    }

    // ------------------- (hardcoded) data voor de foodstands -------------------------
    fun getFoodstandList(): MutableLiveData<List<FoodStand>> {
        return MutableLiveData(listOf(
            FoodStand(
                "hot_dog",
                "Hotter Dogs",
                R.drawable.ic_fastfood
            )
        ,
            FoodStand(
                "burger",
                "Burger Boy's",
                R.drawable.ic_fastfood
            )
        ,
            FoodStand(
                "pizza",
                "Pizza Town",
                R.drawable.ic_pizza
            )
        ,
            FoodStand(
                "burger",
                "Donny's Burgers",
                R.drawable.ic_fastfood
            )
        ,
            FoodStand(
                "pizza",
                "Pizza For You",
                R.drawable.ic_pizza
            )
        ,
            FoodStand(
                "french_fries",
                "Frietjes bij Pol",
                R.drawable.ic_fastfood
            )
        ,
            FoodStand(
                "french_fries",
                "French Fries",
                R.drawable.ic_fastfood
            )
        ,
            FoodStand(
                "pizza",
                "Pizza Lovers",
                R.drawable.ic_pizza
            )
        ,
            FoodStand(
                "kebab",
                "Kebab Shop",
                R.drawable.ic_fastfood
            )
        ,
            FoodStand(
                "french_fries",
                "Friet Shop",

                R.drawable.ic_fastfood
            )
        ,
            FoodStand(
                "burger",
                "Burger Shop",

                R.drawable.ic_fastfood
            )
        ))

    }

    fun getFoodstandMenu(id: String): MutableLiveData<ArrayList<Dish>> {
        val burgerList = ArrayList<Dish>()

        burgerList.add(
            Dish(
                "Classic Burger",
                2,
                false,
                false
            )
        )
        burgerList.add(
            Dish(
                "Cheeseburger",
                2,
                false,
                false
            )
        )
        burgerList.add(
            Dish(
                "Veggie Burger",
                2,
                true,
                false
            )
        )
        burgerList.add(
            Dish(
                "Kingsize Burger",
                4,
                false,
                false
            )
        )
        burgerList.add(
            Dish(
                "Kingsize Cheeseburger",
                4,
                false,
                false
            )
        )
        burgerList.add(
            Dish(
                "Bacon Burger",
                3,
                false,
                false
            )
        )

        val hotDogList = ArrayList<Dish>()

        hotDogList.add(
            Dish(
                "Hot dog",
                1,
                false,
                false
            )
        )
        hotDogList.add(
            Dish(
                "Double hot dog",
                2,
                false,
                false
            )
        )

        val pizzaList = ArrayList<Dish>()

        pizzaList.add(
            Dish(
                "Pizza margherita slice",
                1,
                true,
                false
            )
        )
        pizzaList.add(
            Dish(
                "Pizza margherita 30cm",
                6,
                true,
                false
            )
        )
        pizzaList.add(
            Dish(
                "Pizza hawaii slice",
                2,
                false,
                false
            )
        )
        pizzaList.add(
            Dish(
                "Pizza hawaii 30cm",
                8,
                false,
                false
            )
        )
        pizzaList.add(
            Dish(
                "Pizza prosciutto slice",
                2,
                false,
                false
            )
        )
        pizzaList.add(
            Dish(
                "Pizza prosciutto 30cm",
                8,
                false,
                false
            )
        )
        pizzaList.add(
            Dish(
                "Veggie pizza slice",
                1,
                true,
                false
            )
        )
        pizzaList.add(
            Dish(
                "Veggie pizzza 30cm",
                6,
                true,
                false
            )
        )
        pizzaList.add(
            Dish(
                "Vegan pizza slice",
                1,
                true,
                true
            )
        )
        pizzaList.add(
            Dish(
                "Vegan pizza 30cm",
                6,
                true,
                true
            )
        )

        val frenchFriesList = ArrayList<Dish>()

        frenchFriesList.add(
            Dish(
                "Kleine friet",
                1,
                false,
                false
            )
        )
        frenchFriesList.add(
            Dish(
                "Medium friet",
                2,
                false,
                false
            )
        )
        frenchFriesList.add(
            Dish(
                "Grote friet",
                3,
                false,
                false
            )
        )
        frenchFriesList.add(
            Dish(
                "Bicky Burger",
                2,
                false,
                false
            )
        )
        frenchFriesList.add(
            Dish(
                "Bicky Cheese",
                2,
                false,
                false
            )
        )
        frenchFriesList.add(
            Dish(
                "Bitterballen",
                1,
                false,
                false
            )
        )
        frenchFriesList.add(
            Dish(
                "2x frikandel",
                1,
                false,
                false
            )
        )
        frenchFriesList.add(
            Dish(
                "Mexicano",
                2,
                false,
                false
            )
        )

        val kebabList = ArrayList<Dish>()
        kebabList.add(
            Dish(
                "Kleine pita",
                2,
                false,
                false
            )
        )
        kebabList.add(
            Dish(
                "Grote pita",
                3,
                false,
                false
            )
        )
        kebabList.add(
            Dish(
                "Kleine pita kip",
                2,
                false,
                false
            )
        )
        kebabList.add(
            Dish(
                "Grote pita kip",
                3,
                false,
                false
            )
        )
        kebabList.add(
            Dish(
                "Kleine pita lamsvlees",
                2,
                false,
                false
            )
        )
        kebabList.add(
            Dish(
                "Grote pita lamsvlees",
                3,
                false,
                false
            )
        )

        if (id.equals("pizza")) {
            return MutableLiveData(pizzaList)
        }

        else if (id.equals("kebab")) {
            return MutableLiveData(kebabList)
        }

        else if (id.equals("burger")) {
            return MutableLiveData(burgerList)
        }

        else if (id.equals("french_fries")) {
            return MutableLiveData(frenchFriesList)
        }

        else {
            return MutableLiveData(hotDogList)
        }


    }


// -------------- (hardcoded) data voor newsfeed ------------------

fun getNewsfeedItems(): MutableLiveData<List<NewsfeedItem>> {
    val item1 = NewsfeedItem(
        R.mipmap.bakfietslogo,
        "Bakfiets",
        "16:40",
        R.mipmap.uberdope,
        "Vanavond aan de dope met Uberdope"
    )
    val item2 = NewsfeedItem(
        R.mipmap.bakfietslogo,
        "Bakfiets",
        "16:40",
        R.mipmap.martinipost,
        "Even weg van uw kinderen? Kom genieten aan onze martinistand"
    )
    val item3 = NewsfeedItem(
        R.mipmap.bakfietslogo,
        "Bakfiets",
        "16:40",
        R.mipmap.randanimatie,
        "Uw kinderen even beu? Laat ze achter bij onze volkspelen"
    )

    return MutableLiveData(listOf(item1, item2, item3))
}


companion object {
    @Volatile
    private var instance: FestivalRepository? = null

    fun getInstance(database: FirebaseDatabase) = instance
        ?: synchronized(this) {
            instance
                ?: FestivalRepository(database)
                    .also { instance = it }
        }
}
}