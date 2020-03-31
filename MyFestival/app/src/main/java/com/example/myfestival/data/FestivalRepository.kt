package com.example.myfestival.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FestivalRepository(val database : FirebaseDatabase ) {
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


    fun getFestivalName(): MutableLiveData<String> {
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
        return name
    }


    companion object {
        @Volatile private var instance: FestivalRepository? = null

        fun getInstance(database: FirebaseDatabase) = instance
            ?: synchronized(this) {
                instance
                    ?: FestivalRepository(database).also { instance = it }
            }
    }
}