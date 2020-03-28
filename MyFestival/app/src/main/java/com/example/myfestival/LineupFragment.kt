package com.example.myfestival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfestival.adapters.ConcertAdapter
import kotlinx.android.synthetic.main.lineup_fragment.*
import com.example.myfestival.databinding.LineupFragmentBinding


/**
 * A simple [Fragment] subclass.
 */
class LineupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : LineupFragmentBinding = LineupFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val concert = Concert("Zwangere Guy", "20:30", "22:00")
        val concertList = listOf<Concert>(concert)

        binding.recyclerView.adapter = ConcertAdapter(concertList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.setHasFixedSize(true)

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.lineup_fragment, container, false)
        return binding.root
    }
}
