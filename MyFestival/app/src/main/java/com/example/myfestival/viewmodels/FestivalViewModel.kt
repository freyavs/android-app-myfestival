package com.example.myfestival.viewmodels


import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FestivalViewModel : ViewModel() {
    var name: MutableLiveData<String> = MutableLiveData()

    val TAG = "FIREBASEtag"


    fun getWelcomeString(): LiveData<String> {
        if (name.value == null) {
            Log.d(TAG, "Adding listener to name")
            FirebaseDatabase.getInstance()
                .getReference("-M3b9hJNsFaCXAi8Gegq/name")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //name.postValue(dataSnapshot.value.toString())
                            Log.d(TAG, "getName:onDataChange -> name exists")
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, "getName:onCancelled", databaseError.toException())
                    }
                })
        }
        Log.d(TAG, "getWelcome string: " + name.value)
        return name
    }
}