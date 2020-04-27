package be.ugent.myfestival

import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class BackgroundNotificationService : JobService() {

    companion object {
        const val TAG = "BACKGROUNDSERVICE"
        var jobCancelled: Boolean = false
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
        return true
    }

    fun doBackgroundWork(params: JobParameters?) {
        val festivalId = params?.extras?.getString("festivalId")
        val festivalName = params?.extras?.getString("festivalName")
        Thread(Runnable {
            run {
                Log.d(TAG, "listening")
                    if (festivalId != null) {
                        FirebaseDatabase.getInstance().getReference(festivalId)
                            .child("messages")
                            .addChildEventListener(object : ChildEventListener {
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
                                    val resultIntent = Intent(applicationContext, MainActivity::class.java)
                                    val pendingIntent = PendingIntent.getActivity(applicationContext, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                                    val builder =
                                        NotificationCompat.Builder(applicationContext, MyFestival.CHANNEL_1_ID)
                                            .setSmallIcon(R.drawable.newsfeed_50dp)
                                            .setContentTitle(festivalName?.toUpperCase())
                                            .setContentText(ds.child("title").value.toString())
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setAutoCancel(true)
                                            .setContentIntent(pendingIntent)

                                    with(NotificationManagerCompat.from(applicationContext)) {
                                        notify(1, builder.build())
                                    }
                                }

                                override fun onChildRemoved(p0: DataSnapshot) {
                                    // hier is geen notificatie voor nodig
                                }
                            })
                    }

                }

            }).start()
    }

}