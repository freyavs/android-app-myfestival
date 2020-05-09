package be.ugent.myfestival.adapters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R
import be.ugent.myfestival.notifications.ReminderBroadcast
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
        //voor herschaling van px naar dp
        val dpfactor =
            holder.itemView.context.resources.displayMetrics.density

        //artistnaam
        holder.artistView.text = concert.artist

        //tijdstippen
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val van = "Van: ${concert.start.format(formatter)}"
        val tot = "Tot: ${concert.stop.format(formatter)}"
        holder.startView.text = van
        holder.stopView.text = tot

        val context = holder.itemView.context
        val preference = context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE)
        val switchvalue = preference?.getBoolean(concert.id,false)
        if (switchvalue != null) {
            holder.switch.isChecked = switchvalue
        }
        else {
            //als switchvalue een probleem geeft moet het sowieso op false staan
            holder.switch.isChecked = false
        }

        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                //toestand v/d switch onthouden
                val preference = context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE)
                val editor = preference?.edit()
                editor?.putBoolean(concert.id,true)
                editor?.apply()
                //notificatie instellen
                val _intent = Intent(context, ReminderBroadcast::class.java)
                _intent.putExtra("artist", concert.artist)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, concert.id.hashCode(), _intent, 0)
                val alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                /* om de notificatie te testen op jouw gekozen tijdstip
                calendar.set(Calendar.HOUR_OF_DAY, 14);
                calendar.set(Calendar.MINUTE, 42);
                calendar.set(Calendar.SECOND, 0);*/
                calendar.set(concert.start.year, concert.start.monthValue,concert.start.dayOfMonth, concert.start.hour, concert.start.minute)
                alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
            }
            else {
                //toestand v/d switch onthouden
                val preference = context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE)
                val editor = preference?.edit()
                editor?.putBoolean(concert.id,false)
                editor?.apply()
                //notificatie cancellen
                val _intent = Intent(context, ReminderBroadcast::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, concert.id.hashCode(), _intent, 0)
                val alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
            }
        }
        //size van card
        val minutes: Long =  concert.start.until(concert.stop, ChronoUnit.MINUTES)
        holder.spacer.layoutParams.height = (230 * minutes/60 * dpfactor).toInt()

        //size van pauze
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
        val artistView: TextView = itemView.artist
        val startView: TextView = itemView.start
        val stopView: TextView = itemView.stop
        val spacer: Space = itemView.spacer
        val switch: Switch = itemView.switch1
    }
}