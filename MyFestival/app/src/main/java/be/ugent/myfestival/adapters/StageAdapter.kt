package be.ugent.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.ugent.myfestival.models.Concert
import be.ugent.myfestival.R
import kotlinx.android.synthetic.main.concert_item.view.*

class StageAdapter(private val concertList: List<Concert>) : RecyclerView.Adapter<StageAdapter.ConcertViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcertViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.concert_item, parent, false)

        return ConcertViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ConcertViewHolder, position: Int) {
        val concert = concertList[position]

        holder.textView1.text = concert.artist
        holder.textView2.text = concert.start
        holder.textView3.text = concert.stop
        holder.spacer.layoutParams.height = (1..500).shuffled().first()
    }

    override fun getItemCount() = concertList.size

    class ConcertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView1: TextView = itemView.artist
        val textView2: TextView = itemView.start
        val textView3: TextView = itemView.stop
        val spacer: Space = itemView.spacer
    }

}