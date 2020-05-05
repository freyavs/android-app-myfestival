package be.ugent.myfestival.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.database.FirebaseDatabase

class MyFestival : Application() {
    companion object {
        const val CHANNEL_1_ID = "newsfeed";
        const val CHANNEL_2_ID = "lineup"
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        createNotificationChannels()
    }

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1: NotificationChannel = NotificationChannel(
                CHANNEL_1_ID,
                "Newsfeed notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val channel2: NotificationChannel = NotificationChannel(
                CHANNEL_2_ID,
                "Lineup notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is the newsfeed channel"
            channel2.description = "This is the lineup channel"

            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }
    }
}