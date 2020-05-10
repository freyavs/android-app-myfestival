package be.ugent.myfestival.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.data.FestivalRepositoryInterface
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel

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