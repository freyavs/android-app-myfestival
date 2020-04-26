package be.ugent.myfestival.adapters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R
import be.ugent.myfestival.ReminderBroadcast
import be.ugent.myfestival.models.Concert
import kotlinx.android.synthetic.main.concert_item.view.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class StageAdapter(private val concertList: List<Concert>) : RecyclerView.Adapter<StageAdapter.ConcertViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcertViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.concert_item, parent, false)

        return ConcertViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ConcertViewHolder, position: Int) {
        val concert = concertList[position]

        val dpfactor =
            holder.itemView.context.resources.displayMetrics.density


        holder.textView1.text = concert.artist

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val van = "Van: ${concert.start.format(formatter)}"
        val tot = "Tot: ${concert.stop.format(formatter)}"
        holder.textView2.text = van
        holder.textView3.text = tot

        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            val context = holder.itemView.context
            if(isChecked) {
                val _intent = Intent(context, ReminderBroadcast::class.java)
                _intent.putExtra("artist", concert.artist)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, concert.id.hashCode(), _intent, 0)
                val alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
                val calendar: Calendar = Calendar.getInstance()
                calendar.setTimeInMillis(System.currentTimeMillis())
                calendar.set(Calendar.HOUR_OF_DAY, 14);
                calendar.set(Calendar.MINUTE, 42);
                calendar.set(Calendar.SECOND, 0);
                //calendar.set(concert.start.year, concert.start.monthValue,concert.start.dayOfMonth, concert.start.hour, concert.start.minute)
                alarmManager[AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()] = pendingIntent
            }
            else {
                val _intent = Intent(context, ReminderBroadcast::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, concert.id.hashCode(), _intent, 0)
                val alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
            }
        }

        val minutes: Long =  concert.start.until(concert.stop, ChronoUnit.MINUTES)
        holder.spacer.layoutParams.height = (230 * minutes/60 * dpfactor).toInt()

        if (position < concertList.size - 1) {
            val pause: Long =
                concert.stop.until(concertList[position + 1].start, ChronoUnit.MINUTES)

            val params = (holder.card.layoutParams as ViewGroup.MarginLayoutParams)
            params.bottomMargin = (pause*dpfactor).toInt()
        }

    }

    override fun getItemCount() = concertList.size

    class ConcertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val card: CardView = itemView.card
        val textView1: TextView = itemView.artist
        val textView2: TextView = itemView.start
        val textView3: TextView = itemView.stop
        val spacer: Space = itemView.spacer
        val switch: Switch = itemView.switch1
    }
}