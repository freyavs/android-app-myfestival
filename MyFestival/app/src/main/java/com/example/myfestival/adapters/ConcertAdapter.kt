package com.example.myfestival.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfestival.Concert
import com.example.myfestival.R
import kotlinx.android.synthetic.main.concert_fragment.view.*
import kotlinx.android.synthetic.main.lineup_fragment.view.*

class ConcertAdapter(private val concertList: List<Concert>) : RecyclerView.Adapter<ConcertAdapter.ConcertViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcertViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.concert_fragment, parent, false)

        return ConcertViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConcertViewHolder, position: Int) {
        val concert = concertList[position]

        holder.textView1.text = concert.artist
        holder.textView2.text = concert.start
        holder.textView3.text = concert.artist
    }

    override fun getItemCount() = concertList.size

    class ConcertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView1: TextView = itemView.artist
        val textView2: TextView = itemView.start
        val textView3: TextView = itemView.stop
    }

}