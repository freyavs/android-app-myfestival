package be.ugent.myfestival.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import be.ugent.myfestival.R
import be.ugent.myfestival.models.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FestivalRepository(val database: FirebaseDatabase) {
    var name: MutableLiveData<String> = MutableLiveData()

    var newsfeed: MutableLiveData<List<NewsfeedItem>> = MutableLiveData()

    var foodstands: MutableLiveData<List<FoodStand>> = MutableLiveData()
    var festivalList: MutableLiveData<List<FestivalChooser>> = MutableLiveData()

    var test: MutableLiveData<String> = MutableLiveData()

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
            FirebaseDatabase.getInstance()
                .getReference(festivalID).child("name")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            name.postValue(dataSnapshot.value.toString())
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "getName:onCancelled", databaseError.toException())
                    }
                })
        }
        return name
    }

    // ----------------------------- data voor de foodstands -------------------------------
    fun getFoodstandList() : MutableLiveData<List<FoodStand>> {
        if (foodstands.value == null) {
            addConnectionListener()
            FirebaseDatabase.getInstance()
                .getReference(festivalID).child("foodstand").orderByKey()
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            var foodList = mutableListOf<FoodStand>()
                            for (ds in dataSnapshot.children) {
                                //TODO: logo
                                var dishList = mutableListOf<Dish>()
                                ds.child("menu").children.mapNotNullTo(dishList) {
                                    it.getValue(Dish::class.java)
                                }
                                foodList.add (FoodStand(
                                    ds.key!!,
                                    ds.child("name").value!!.toString(),
                                    R.drawable.ic_fastfood,
                                    dishList
                                ))
                            }
                            foodstands.postValue(foodList)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "getFoodstandList:onCancelled", databaseError.toException())
                    }
                })
        }
        return foodstands
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

    // -------------- (hardcoded) data voor festival chooser ------------------
    fun getFestivals(): MutableLiveData<List<FestivalChooser>>{
        if(festivalList.value == null){
            addConnectionListener()
            FirebaseDatabase.getInstance()
                .getReference()
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.exists()){
                            val festivalChoosers = mutableListOf<FestivalChooser>()
                            for(ds in dataSnapshot.children){
                                festivalChoosers.add(
                                    FestivalChooser(
                                        ds.key!!,
                                        ds.child("name").value!!.toString()
                                    )
                                )
                            }
                            festivalList.postValue(festivalChoosers)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "foutje", databaseError.toException())
                    }
                })
        }
        return festivalList
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