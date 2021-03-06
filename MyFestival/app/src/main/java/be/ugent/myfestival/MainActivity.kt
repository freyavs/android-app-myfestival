package be.ugent.myfestival

import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import be.ugent.myfestival.notifications.BackgroundNotificationService
import be.ugent.myfestival.notifications.MyFestival
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: FestivalViewModel
    private val TAG = "myFestivalTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel by viewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }
        this.viewModel = viewModel

       viewModel.getNewMessageTitle().observe(this, Observer { message ->
           Log.d(TAG, "creating notification: " + message)
               createNotification(viewModel.getFestivalName().value.toString(), message)

        })


       val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onStart() {
        super.onStart()
        val jobScheduler: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(123)
    }

    override fun onStop() {
        super.onStop()
        val bundle: PersistableBundle = PersistableBundle()
        bundle.putString("festivalId", viewModel.getCurrentFestivalId())
        bundle.putString("festivalName", viewModel.getFestivalName().value.toString())
        val componentName: ComponentName = ComponentName(this, BackgroundNotificationService::class.java)
        val jobInfo: JobInfo = JobInfo.Builder(123, componentName)
            .setPersisted(true)
            .setRequiresCharging(false)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setPeriodic(15 * 60 * 1000)
            .setExtras(bundle)
            .build()
        val jobScheduler: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeListeners()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun createNotification(title: String, message: String) {
        val builder =
            NotificationCompat.Builder(this, MyFestival.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.newsfeed_50dp)
                .setContentTitle(title.toUpperCase())
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            val randomGen: Random = Random()
            val id = randomGen.nextInt(100)

        with(NotificationManagerCompat.from(this)) {
            notify(id, builder.build())
            }

    }
}

    



