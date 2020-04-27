package be.ugent.myfestival

import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: FestivalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel by viewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }
        this.viewModel = viewModel

       viewModel.getNewMessageTitle().observe(this, Observer {
                message -> createNotification(viewModel.getFestivalName().value.toString(), message)
                Log.d("NOTIFICATION", "message wordt doorgegeven")
        })


       val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //zorgt voor offline gegevens 
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    override fun onStart() {
        super.onStart()
        val jobScheduler: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(123)
    }

    override fun onStop() {
        super.onStop()
        Log.d("BACKGROUNDSERVICE", "app destroyed")
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
        val resultCode = jobScheduler.schedule(jobInfo)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("BACKGROUNDSERVICE", "job scheduled")
        }
        else {
            Log.d("BACKGROUNDSERVICE", "job scheduling failed")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun createNotification(title: String, message: String) {
        Log.d("NOTIFICATION", "geraakt tot in notificatie aanmaken methode")
        if (message != "") {
            val resultIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder =
            NotificationCompat.Builder(this, MyFestival.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.newsfeed_50dp)
                .setContentTitle(title.toUpperCase())
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
            }
        }
    }
}

    



