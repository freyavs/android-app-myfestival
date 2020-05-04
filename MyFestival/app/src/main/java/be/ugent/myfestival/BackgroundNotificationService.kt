package be.ugent.myfestival

import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.*
import java.util.*

class BackgroundNotificationService : JobService() {

    var listener: ChildEventListener? = null

    companion object {
        const val TAG = "BACKGROUNDSERVICE"
        var jobCancelled: Boolean = false
        var incomingDataCount = 0
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "background job started")
        jobCancelled = false
        doBackgroundWork(params)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        val festivalId = params?.extras?.getString("festivalId")
        if (festivalId != null) {
            FirebaseDatabase.getInstance().getReference(festivalId)
                .child("messages")
                .removeEventListener(this.listener!!)
        }
        return true
    }

    fun doBackgroundWork(params: JobParameters?) {
        val festivalId = params?.extras?.getString("festivalId")
        val festivalName = params?.extras?.getString("festivalName")
        var newsfeedLoaded = false
        Thread(Runnable {
            run {
                Log.d(TAG, "listening")
                        this.listener = object : ChildEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            // hier is geen notificatie voor nodig
                        }

                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                            // zal nooit gebeuren, er is dus geen notificatie voor nodig
                        }

                        override fun onChildChanged(ds: DataSnapshot, p1: String?) {
                            onChildAdded(ds, p1)
                        }

                        override fun onChildAdded(ds: DataSnapshot, p1: String?) {
                            if (newsfeedLoaded) {
                                val resultIntent =
                                    Intent(applicationContext, MainActivity::class.java)
                                val pendingIntent = PendingIntent.getActivity(
                                    applicationContext,
                                    1,
                                    resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                                )
                                val builder =
                                    NotificationCompat.Builder(
                                        applicationContext,
                                        MyFestival.CHANNEL_1_ID
                                    )
                                        .setSmallIcon(R.drawable.newsfeed_50dp)
                                        .setContentTitle(festivalName?.toUpperCase())
                                        .setContentText(ds.child("title").value.toString())
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setAutoCancel(true)
                                        .setContentIntent(pendingIntent)

                                val randomGen: Random = Random()
                                val id = randomGen.nextInt(100)

                                with(NotificationManagerCompat.from(applicationContext)) {
                                    notify(id, builder.build())

                                }

                            }
                            incomingDataCount +=1
                        }

                        override fun onChildRemoved(p0: DataSnapshot) {
                                // hier is geen notificatie voor nodig
                        }
                        }


                if (festivalId != null) {
                    FirebaseDatabase.getInstance().getReference(festivalId)
                        .child("messages")
                        .addChildEventListener(this.listener as ChildEventListener)

                    //zelfde redenering als in repository: wanneer value event wordt opgeroepen zijn alle initiele children al geladen
                    FirebaseDatabase.getInstance().getReference(festivalId).child("messages")
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
            }

        }).start()
    }

}