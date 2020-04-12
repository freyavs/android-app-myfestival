package be.ugent.myfestival.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import be.ugent.myfestival.models.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.time.LocalDateTime

class FestivalRepository(val database: FirebaseDatabase) {
    var name: MutableLiveData<String> = MutableLiveData()
    var newsfeed: MutableLiveData<MutableList<NewsfeedItem>> = MutableLiveData()
    var foodstands: MutableLiveData<List<FoodStand>> = MutableLiveData()
    var lineupstages: MutableLiveData<List<Stage>> = MutableLiveData()
    var logo: MutableLiveData<String> = MutableLiveData()

    val TAG = "FestivalRepository"

    val storageRef = Firebase.storage.reference

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

    // ------------- data voor het festival algemeen (home menu, ..)  -------------------------------------
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

    fun getFestivalLogo(): MutableLiveData<String> {
        if (logo.value == null) {
            FirebaseDatabase.getInstance()
                .getReference(festivalID).child("logo")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(ds: DataSnapshot) {
                        if (ds.exists()) {
                            //todo: eerst vorige tempfile verwijderen ofzo (logo wordt wss toch nooit aangepast eig)
                            val logoRef = storageRef.child(ds.value.toString())
                            Log.d(TAG, "logo: " + ds.value.toString())
                            val localFile = File.createTempFile("festival_logo", ".png")
                            logoRef.getFile(localFile).addOnSuccessListener {
                                logo.postValue(localFile.absolutePath)
                                Log.d(TAG, "Tempfile created for logo of festival.")
                            }.addOnFailureListener {
                                Log.d(TAG, "Tempfile failed: check if festival submitted a logo!")
                            }
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "getFestivalLogo:onCancelled", databaseError.toException())
                    }
                })
        }
        return logo
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
                                //todo: cache legen zodat geen dubbele foto's worden opgeslaan of mss zelfs gewoon geen foto's opslaan ( getCacheDir )
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
            //todo: sort on timestamp & timestamp aflezen uit databank ofc
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

                    //todo: rest van volgende functies invullen

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

    // -------------------------- data voor de lineup ------------------------

    fun getLineup() : MutableLiveData<List<Stage>> {
        if (lineupstages.value == null) {
            addConnectionListener()
            FirebaseDatabase.getInstance()
                .getReference(festivalID).child("stages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val stages = mutableListOf<Stage>()
                            for (ds in dataSnapshot.children) {
                                val concerts = mutableListOf<Concert>()
                                for (dss in ds.child("concerts").children) {
                                   concerts.add(Concert(
                                        dss.child("artist").value.toString(),
                                        LocalDateTime.parse(dss.child("startdate").value.toString()),
                                        LocalDateTime.parse(dss.child("enddate").value.toString())
                                    ))
                                }
                                stages.add(Stage(
                                    ds.child("name").value.toString(),
                                    concerts
                                ))
                            }
                            lineupstages.postValue(stages)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "getLineup:onCancelled", databaseError.toException())
                    }
                })
        }
        return lineupstages
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