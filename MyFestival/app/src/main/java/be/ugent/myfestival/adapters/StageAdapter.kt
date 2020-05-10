package be.ugent.myfestival.adapters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R
import be.ugent.myfestival.notifications.ReminderBroadcast
import be.ugent.myfestival.models.Concert
import kotlinx.android.synthetic.main.concert_item.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

        //artistnaam
        holder.artistView.text = concert.artist

        //tijdstippen
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val van = "Van: ${concert.start.format(formatter)}"
        val tot = "Tot: ${concert.stop.format(formatter)}"
        holder.startView.text = van
        holder.stopView.text = tot

        //voor herschaling van px naar dp
        val dpfactor =
            holder.itemView.context.resources.displayMetrics.density

        //ruimte tussen listitems, maar niet voor de laatste
        if (position < concertList.size - 1) {
            val params = (holder.card.layoutParams as ViewGroup.MarginLayoutParams)
            params.bottomMargin = (20*dpfactor).toInt()
        }

        //notificaties
        val context = holder.itemView.context
        val preference = context.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE)
        holder.switch.isChecked = preference.getBoolean(concert.id,false)

        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                //toestand v/d switch onthouden
                val editor = preference.edit()
                editor.putBoolean(concert.id,true)
                editor.apply()
                //notificatie instellen
                val intent = Intent(context, ReminderBroadcast::class.java)
                intent.putExtra("artist", concert.artist)
                val pendingIntent = PendingIntent.getBroadcast(context, concert.id.hashCode(), intent, 0)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)

                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                if(concert.start > LocalDateTime.now()) {
                    calendar.set(
                        concert.start.year,
                        concert.start.monthValue,
                        concert.start.dayOfMonth,
                        concert.start.hour,
                        concert.start.minute
                    )
                }
                alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
            }
            else {
                //toestand v/d switch onthouden
                val editor = preference.edit()
                editor.putBoolean(concert.id,false)
                editor.apply()
                //notificatie cancellen
                val intent = Intent(context, ReminderBroadcast::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, concert.id.hashCode(), intent, 0)
                val alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
            }
        }
    }

    override fun getItemCount() = concertList.size

    class ConcertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val card: CardView = itemView.card
        val artistView: TextView = itemView.artist
        val startView: TextView = itemView.start
        val stopView: TextView = itemView.stop
        val switch: Switch = itemView.switch1
    }
}