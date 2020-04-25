package be.ugent.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.R
import be.ugent.myfestival.models.Concert
import kotlinx.android.synthetic.main.concert_item.view.*
import kotlinx.android.synthetic.main.stage_fragment.view.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
    }

}