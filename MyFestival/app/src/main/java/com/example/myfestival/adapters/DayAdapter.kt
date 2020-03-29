package com.example.myfestival.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfestival.R
import com.example.myfestival.Stage
import kotlinx.android.synthetic.main.stage_item.view.*



class DayAdapter(private val stageList: List<Stage>) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stage_item, parent, false)

        context = parent.context

        return DayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val stage = stageList[position]

        holder.stageRecycler.adapter = com.example.myfestival.adapters.StageAdapter(stage.concerts)
        holder.stageRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.stageRecycler.setHasFixedSize(true)


    }

    override fun getItemCount() = stageList.size

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val stageRecycler: RecyclerView = itemView.stage_recycler
    }

}
