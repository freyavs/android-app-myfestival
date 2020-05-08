package be.ugent.myfestival.data

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.ugent.myfestival.models.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.xml.transform.TransformerFactory

class FestivalRepository(val database: FirebaseDatabase, val storageRef: StorageReference) : FestivalRepositoryInterface {
    var name: MutableLiveData<String> = MutableLiveData()

    var nameListener: ValueEventListener? = null

    var newsfeed: MutableLiveData<MutableList<NewsfeedItem>> = MutableLiveData()
    var newMessageTitle: MutableLiveData<String> = MutableLiveData()
    var newsfeedListener: ChildEventListener? = null


    var foodstands: MutableLiveData<List<FoodStand>> = MutableLiveData()
    var foodstandsListener: ValueEventListener? = null

    var festivalList: MutableLiveData<List<FestivalChooser>> = MutableLiveData()
    var lineupstages: MutableLiveData<List<Stage>> = MutableLiveData()
    var festivalListListener: ValueEventListener? = null

    var lineupstagesListener: ValueEventListener? = null


    var logo: MutableLiveData<StorageReference> = MutableLiveData()
    var logoListener: ValueEventListener? = null

    var map: MutableLiveData<String> = MutableLiveData()
    var mapListener: ValueEventListener? = null

    var coords: MutableLiveData<List<Double>> = MutableLiveData()
    var concertsCoords: MutableLiveData<HashMap<String, List<Double>>> = MutableLiveData()

    val TAG = "myFestivalTag"

    var festivalID = ""

    var newsfeedLoaded = false


    // -------------------------- als id wordt gezet --------------------------

    override fun reset(oldId: String) {
        Log.d(TAG, "RESETTING with old id: " + oldId)
        name = MutableLiveData()
        newsfeed = MutableLiveData()
        foodstands = MutableLiveData()
        lineupstages = MutableLiveData()
        logo = MutableLiveData()
        map = MutableLiveData()
        coords = MutableLiveData()
        concertsCoords = MutableLiveData()
        newsfeedLoaded = false

        removeListeners(oldId)

        getFoodstandList()
        getNewsfeedItems()
        getFestivalMap()
        getLineup()
    }

    override fun removeListeners(oldId: String){
        if (oldId != "") {
            Log.d(TAG, " -------- Removing listeners --------- ")
            val ref = database.getReference(oldId)
            ref.child("messages").removeEventListener(newsfeedListener!!)
            ref.child("name").removeEventListener(nameListener!!)
            ref.child("logo").removeEventListener(logoListener!!)
            ref.child("foodstand").removeEventListener(foodstandsListener!!)
            ref.child("location").removeEventListener(mapListener!!)
            ref.child("stages").removeEventListener(lineupstagesListener!!)
            if (festivalListListener != null){
                ref.removeEventListener(festivalListListener!!)
            }
        }
    }


    override fun getId(): String {
        return festivalID
    }

    override fun setId(id : String) {
        festivalID = id
    }

    // ------------- data voor het festival algemeen (home menu, ..)  ----------------
    override fun getFestivalName(): MutableLiveData<String> {
        if (name.value == null) {
            nameListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        name.postValue(dataSnapshot.value.toString())
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, "getName:onCancelled", databaseError.toException())
                }
            }
            database
                .getReference(festivalID).child("name")
                .addValueEventListener(nameListener!!)
        }
        return name
    }

    override fun getFestivalLogo(): MutableLiveData<StorageReference> {
        if (logo.value == null) {
            logoListener = object : ValueEventListener {
                override fun onDataChange(ds: DataSnapshot) {
                    if (ds.exists()) {
                        val logoRef = storageRef.child(ds.value.toString())
                        Log.d(TAG, "logo: " + ds.value.toString())
                        logo.postValue(logoRef)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, "getFestivalLogo:onCancelled", databaseError.toException())
                }
            }
            database
                .getReference(festivalID).child("logo")
                .addValueEventListener(logoListener!!)
        }
        return logo
    }

    override fun getFestivalMap(): MutableLiveData<String> {
        if (map.value == null) {
            mapListener = object : ValueEventListener {
                override fun onDataChange(ds: DataSnapshot) {
                    if (ds.exists()) {
                        val logoRef = storageRef.child(ds.value.toString())
                        Log.d(TAG, "location: " + ds.value.toString())
                        val localFile = File.createTempFile("festival_map", ".png")
                        logoRef.getFile(localFile).addOnSuccessListener {
                            map.postValue(localFile.absolutePath)
                            Log.d(TAG, "Tempfile created for map of festival.")
                        }.addOnFailureListener {
                            Log.d(TAG, "Tempfile failed: check if festival submitted a map!")
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, "getFestivalLogo:onCancelled", databaseError.toException())
                }
            }
            database
                .getReference(festivalID).child("location")
                .addValueEventListener(mapListener!!)
        }
        return map
    }
    fun getCoordsFestival(): MutableLiveData<List<Double>> {
        if (coords.value == null){
            val co = mutableListOf<Double>()
            database
                .getReference(festivalID).child("coords")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        co.add(dataSnapshot.child("lat").value.toString().toDouble())
                        co.add(dataSnapshot.child("long").value.toString().toDouble())
                        coords.postValue(co)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "it failed")
                    }
                })
        }
        return coords
    }

    fun getStageCoords(): MutableLiveData<HashMap<String, List<Double>>> {
        if(concertsCoords.value == null){
            var cco: HashMap<String, List<Double>> = HashMap<String, List<Double>>()
            database
                .getReference(festivalID).child("stages")
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children){
                            val co = mutableListOf<Double>()
                            val name = ds.child("name").value.toString()
                            co.add(ds.child("coords").child("lat").value.toString().toDouble())
                            co.add(ds.child("coords").child("long").value.toString().toDouble())
                            cco.put(name,co)
                        }
                        concertsCoords.postValue(cco)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "it failed")
                    }
                })
        }
        return concertsCoords
    }

    // ---------------- data voor de foodstands -------------------------------
    override fun getFoodstandList() : MutableLiveData<List<FoodStand>> {
        if (foodstands.value == null) {
            foodstandsListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        var foodList = mutableListOf<FoodStand>()
                        for (ds in dataSnapshot.children) {
                            val logoRef = storageRef.child(ds.child("image").value.toString())

                            //haal al het eten van een bepaalde foodstand af
                            var dishList = mutableListOf<Dish>()
                            ds.child("menu").children.mapNotNullTo(dishList) {
                                val dish = it.getValue(Dish::class.java)
                                dish!!.id = it.key.toString()
                                dish
                            }
                            //pas als image ingeladen is, maak foodstand aan
                            foodList.add (FoodStand(
                                ds.key!!,
                                ds.child("name").value!!.toString(),
                                logoRef,
                                dishList
                            ))
                            foodstands.postValue(foodList)

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, "getFoodstandList:onCancelled", databaseError.toException())
                }
            }
            database
                .getReference(festivalID).child("foodstand").orderByKey()
                .addValueEventListener(foodstandsListener!!)
        }
        return foodstands
    }

    // ---------------------- data voor de newsfeed -------------------------------

    // werk hier met childEventListener aangezien er vaak zal toegevoegd etc worden (ivm met andere data die bijna niet zal veranderen)

    override fun getNewsfeedItems(): MutableLiveData<MutableList<NewsfeedItem>> {
        if (newsfeed.value.isNullOrEmpty()) {
            newsfeed.value = mutableListOf()

            newsfeedListener = object : ChildEventListener {
                override fun onChildAdded(ds: DataSnapshot, previousChildName: String?) {
                    var list = newsfeed.value!! //veilig want hierboven maken we al lijst aan voor newsfeed

                    val image = ds.child("image").value
                    var reference : StorageReference? = null
                    if (image != null) {
                        reference = storageRef.child(image.toString())
                    }

                    val date = Instant.parse( ds.child("date").value.toString())
                        .atOffset( ZoneOffset.UTC )
                        .format(
                            DateTimeFormatter.ofPattern( "uuuu-MM-dd'T'HH:mm:ss" )
                        )

                    list.add(NewsfeedItem(
                        ds.key.toString(),
                        LocalDateTime.parse(date),
                        reference,
                        ds.child("message").value.toString(),
                        ds.child("title").value.toString()
                    ))

                    list.sortByDescending{it.time}
                    newsfeed.postValue(list)
                    if (newsfeedLoaded) {
                        newMessageTitle.postValue(ds.child("title").value.toString())
                    }
                    Log.d(TAG, "Newsfeed: added newsfeed message")
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    onChildRemoved(dataSnapshot)
                    onChildAdded(dataSnapshot, previousChildName)
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    var updatedList : MutableList<NewsfeedItem> =  mutableListOf()
                    Transformations.map(newsfeed) { list ->
                        updatedList = (list.filter { it.id != dataSnapshot.key}).toMutableList()
                    }
                    newsfeed.postValue(updatedList)
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    //dit moet niets doen want newsfeedposts zullen niet verplaatst kunnen worden
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "getNewsfeedItems:onCancelled", databaseError.toException())
                }
            }

                database
                .getReference(festivalID).child("messages")
                .addChildEventListener(newsfeedListener!!)

                /*
                de childevent listeners zullen eerst getriggered worden -> als alle initiele waarden ingelezen zijn wordt de onderstaande
                valueevent listener opgeroepen, is de newsfeed dus ingeladen en pas vanaf dan mogen er notificatis aangemaakt worden voor
                NIEUWE newsfeed posts
                - een 2de listener hierop zetten is niet erg aangezien firebase de data cached dus alle newsfeed items zullen niet opnieuw
                door de internet verbinding moeten opgehaald worden
                 */
                database.getReference(festivalID).child("messages")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                           newsfeedLoaded = true
                            Log.d(TAG, "Newsfeed: All newsfeed items have loaded.")
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            //doe niets
                        }
                    })
        }
        return newsfeed
    }

    fun resetNewMessageTitle() {
        newMessageTitle.postValue("")
    }
    // -------------------------- data voor de lineup ------------------------

    override fun getLineup() : MutableLiveData<List<Stage>> {
        if (lineupstages.value.isNullOrEmpty()) {
            lineupstagesListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.d(TAG, "catching lineup")
                                val stages = mutableListOf<Stage>()
                                for (ds in dataSnapshot.children) {
                                    val concerts = mutableListOf<Concert>()
                                    for (dss in ds.child("concerts").children) {
                                        concerts.add(Concert(
                                            dss.key.toString(),
                                            dss.child("artist").value.toString(),
                                            LocalDateTime.parse(dss.child("startdate").value.toString()),
                                            LocalDateTime.parse(dss.child("enddate").value.toString())
                                        ))
                                    }
                                    stages.add(Stage(
                                        ds.key.toString(),
                                        ds.child("name").value.toString(),
                                        concerts
                                    ))
                                    lineupstages.postValue(stages)
                                }
                            }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, "getLineup:onCancelled", databaseError.toException())
                }
            }
            database
                .getReference(festivalID).child("stages")
                .addValueEventListener(lineupstagesListener!!)
        }
        return lineupstages
    }

    // -------------- data voor festival chooser ------------------
    override fun getFestivals(): MutableLiveData<List<FestivalChooser>>{
        if(festivalList.value == null){
            festivalListListener = object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        val festivalChoosers = mutableListOf<FestivalChooser>()
                        for(ds in dataSnapshot.children) {
                            val logoRef = storageRef.child(ds.child("logo").value.toString())
                            festivalChoosers.add(
                                FestivalChooser(
                                    ds.key!!,
                                    ds.child("name").value!!.toString(),
                                    logoRef ))
                            festivalList.postValue(festivalChoosers)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, "getFestivals:onCancelled", databaseError.toException())
                }
            }
            database
                .reference
                .addValueEventListener(festivalListListener!!)
        }
        return festivalList
    }

companion object {
    @Volatile
    private var instance: FestivalRepository? = null

    fun getInstance(database: FirebaseDatabase, storageRef: StorageReference) = instance
        ?: synchronized(this) {
            instance
                ?: FestivalRepository(database, storageRef)
                    .also { instance = it }
        }
    }
}