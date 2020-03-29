package com.example.myfestival.viewmodels


import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FestivalViewModel : ViewModel() {
    var name: MutableLiveData<String> = MutableLiveData()

    val TAG = "FIREBASEtag"

    //voor debug redenen:
    val connectedRef = Firebase.database.getReference(".info/connected")
    fun addConnectionListener(){
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


    fun getWelcomeString(): MutableLiveData<String> {
        if (name.value == null) {
            addConnectionListener()
            Log.d(TAG, "Adding listener to name")
            FirebaseDatabase.getInstance()
                .getReference("-M3b9hJNsFaCXAi8Gegq").child("name")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            name.value = dataSnapshot.value.toString()
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