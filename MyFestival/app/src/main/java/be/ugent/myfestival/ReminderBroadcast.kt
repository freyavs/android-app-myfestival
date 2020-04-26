package be.ugent.myfestival

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
            val artist = intent?.getStringExtra("artist")
           val builder : NotificationCompat.Builder? =
               context?.let {
                   NotificationCompat.Builder(it, MyFestival.CHANNEL_1_ID)
                       .setSmallIcon(R.drawable.lineup_50dp)
                       .setContentTitle("$artist begint bijna")
                       .setContentText("Het concert van $artist begint nu!")
                       .setPriority(NotificationCompat.PRIORITY_HIGH)
               }
        if (builder != null) {
            val notificationManger : NotificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManger.notify(200, builder.build())
        }
    }
}