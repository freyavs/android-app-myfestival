package com.example.myfestival.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter


class DayAdapter(private val stageFragmentList: List<Fragment>, manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return stageFragmentList[position]
    }

    override fun getCount(): Int {
        return stageFragmentList.size
    }
}
