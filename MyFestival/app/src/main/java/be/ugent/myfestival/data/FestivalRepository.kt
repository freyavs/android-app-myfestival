package be.ugent.myfestival.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import be.ugent.myfestival.R
import be.ugent.myfestival.models.*
import be.ugent.myfestival.utilities.InjectorUtils
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File


class FestivalRepository(val database: FirebaseDatabase) {
    var name: MutableLiveData<String> = MutableLiveData()
    var newsfeed: MutableLiveData<MutableList<NewsfeedItem>> = MutableLiveData()
    var foodstands: MutableLiveData<List<FoodStand>> = MutableLiveData()
    var festivalList: MutableLiveData<List<FestivalChooser>> = MutableLiveData()
    var test: MutableLiveData<String> = MutableLiveData()

    val TAG = "FestivalRepository"

    val storageRef = Firebase.storage.reference

    //TODO: deadline 2 - festivalID kunnen kiezen
    var festivalID = "Null"

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

    // ---------------- data voor de foodstands -------------------------------
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
                                //todo: cache legen zodat geen dubbele foto's worden opgeslaan ( getCacheDir )
                                val logoRef = storageRef.child(ds.child("image").value.toString())
                                val localFile = File.createTempFile("foodstand", ".png")

                                //haal al het eten van een bepaalde foodstand af
                                var dishList = mutableListOf<Dish>()
                                ds.child("menu").children.mapNotNullTo(dishList) {
                                    it.getValue(Dish::class.java)
                                }
                                logoRef.getFile(localFile).addOnSuccessListener {
                                    //pas als image ingeladen is, maak foodstand aan
                                    foodList.add (FoodStand(
                                        ds.key!!,
                                        ds.child("name").value!!.toString(),
                                        localFile.absolutePath,
                                        dishList
                                    ))
                                }.addOnFailureListener {
                                    Log.d(TAG, "Tempfile failed: check if foodstand submitted a logo!")
                                }
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

    // ---------------------- data voor de newsfeed -------------------------------

    // werk hier met childEventListener aangezien er vaak zal toegevoegd etc worden (ivm met andere data die bijna niet zal veranderen)

    fun getNewsfeedItems(): MutableLiveData<MutableList<NewsfeedItem>> {
        if (newsfeed.value == null) {
            //todo: sort on timestamp!
            newsfeed.value = mutableListOf()
            FirebaseDatabase.getInstance()
                .getReference(festivalID).child("messages")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(ds: DataSnapshot, previousChildName: String?) {
                        var list = newsfeed.value!! //veilig want hierboven maken we al lijst aan voor newsfeed

                        val image = ds.child("image").value
                        var reference : StorageReference? = null
                        if (image != null) {
                            reference = storageRef.child(image.toString())
                        }

                        list.add(NewsfeedItem(
                            "16:40",
                            reference,
                            ds.child("message").value.toString(),
                            ds.child("title").value.toString()
                        ))
                        newsfeed.postValue(list)
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                        Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")
                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so displayed the changed comment.
                        val newComment = dataSnapshot.getValue()
                        val commentKey = dataSnapshot.key
                    }

                    override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                        Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so remove it.
                    }

                    override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                        Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                        // A comment has changed position, use the key to determine if we are
                        // displaying this comment and if so move it.
                        val movedComment = dataSnapshot.getValue()
                        val commentKey = dataSnapshot.key
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w(TAG, "getNewsfeedItems:onCancelled", databaseError.toException())
                    }
            })
        }
        return newsfeed
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

    /*
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
*/


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