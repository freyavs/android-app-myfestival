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
    var newsfeedLoaded = false

    var foodstands: MutableLiveData<List<FoodStand>> = MutableLiveData()
    var foodstandsListener: ValueEventListener? = null

    var festivalList: MutableLiveData<List<FestivalChooser>> = MutableLiveData()
    var festivalListListener: ValueEventListener? = null

    var lineupstages: MutableLiveData<List<Stage>> = MutableLiveData()
    var lineupstagesListener: ValueEventListener? = null

    var logo: MutableLiveData<StorageReference> = MutableLiveData()
    var logoListener: ValueEventListener? = null

    var coords: MutableLiveData<List<Double>> = MutableLiveData()
    var concertsCoords: MutableLiveData<HashMap<String, List<Double>>> = MutableLiveData()
    var foodstandsCoords: MutableLiveData<HashMap<String, List<Double>>> = MutableLiveData()

    val TAG = "myFestivalTag"

    var festivalID = ""



    // -------------------------- als id wordt gezet --------------------------

    override fun reset(oldId: String) {
        //zet alles terug op null
        name = MutableLiveData()
        newsfeed = MutableLiveData()
        foodstands = MutableLiveData()
        lineupstages = MutableLiveData()
        logo = MutableLiveData()
        coords = MutableLiveData()
        concertsCoords = MutableLiveData()
        newsfeedLoaded = false

        //verwijder eventueel de oude listeners
        removeListeners(oldId)

        //laad de nieuwe data in
        getFoodstandList()
        getNewsfeedItems()
        getLineup()
        getCoordsFestival()
        getStageCoords(true)
        getStageCoords(false)
    }

    override fun removeListeners(oldId: String){
        //remove enkel de listeners als het vorige festival bestond
        if (oldId != "") {
            val ref = database.getReference(oldId)
            ref.child("messages").removeEventListener(newsfeedListener!!)
            ref.child("name").removeEventListener(nameListener!!)
            ref.child("logo").removeEventListener(logoListener!!)
            ref.child("foodstand").removeEventListener(foodstandsListener!!)
            ref.child("stages").removeEventListener(lineupstagesListener!!)

            //alle listeners zullen nooit null zijn behalve de listener op de volledige lijst van festival
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

    fun getCoordsFestival(): MutableLiveData<List<Double>> {
        if (coords.value == null){
            val co = mutableListOf<Double>()
            database
                .getReference(festivalID).child("coords")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val lat = dataSnapshot.child("lat").value
                        val long = dataSnapshot.child("long").value
                        //als geen coordinaten heeft, niet toevoegen aan kaart
                        if (lat != null && long != null) {
                            co.add(lat.toString().toDouble())
                            co.add(long.toString().toDouble())
                            coords.postValue(co)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "getName:onCancelled", databaseError.toException())
                    }
                })
        }
        return coords
    }

    fun getStageCoords(stage: Boolean): MutableLiveData<HashMap<String, List<Double>>> {
        val searchString: String
        val returnVariable: MutableLiveData<HashMap<String, List<Double>>>
        if(stage){
            searchString = "stages"
            returnVariable = concertsCoords
        }
        else {
            searchString = "foodstand"
            returnVariable = foodstandsCoords
        }
        val coordsMap: HashMap<String, List<Double>> = HashMap()
        database
            .getReference(festivalID).child(searchString)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children){
                        val co = mutableListOf<Double>()
                        val name = ds.child("name").value.toString()
                        val lat = ds.child("coords").child("lat").value
                        val long = ds.child("coords").child("long").value
                        //als geen coordinaten heeft, niet toevoegen aan kaart
                        if (lat != null && long != null) {
                            co.add(lat.toString().toDouble())
                            co.add(long.toString().toDouble())
                            coordsMap[name] = co
                        }
                    }
                    returnVariable.postValue(coordsMap)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, "getName:onCancelled", databaseError.toException())
                }
            })
        return returnVariable
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
                door de internet verbinding moeten opgehaald worden, en addListenerForSingleValueEvent zal maar 1x uitvoeren dus we moeten
                die listener niet verwijderen
                 */
                database.getReference(festivalID).child("messages")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                           newsfeedLoaded = true
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            //doe niets
                        }
                    })
        }
        return newsfeed
    }

    // -------------------------- data voor de lineup ------------------------

    override fun getLineup() : MutableLiveData<List<Stage>> {
        if (lineupstages.value.isNullOrEmpty()) {
            lineupstagesListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
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