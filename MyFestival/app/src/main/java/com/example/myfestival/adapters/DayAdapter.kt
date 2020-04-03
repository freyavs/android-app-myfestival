package com.example.myfestival.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.myfestival.StageFragment
import com.example.myfestival.models.Stage


class DayAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    private var currentStages = emptyList<Stage>()
    /*
     * Inhoud van dayadapter gebaseerd op: https://stackoverflow.com/questions/10396321/remove-fragment-page-from-viewpager-in-android
     */

    var baseId: Long = 0

    override fun getItem(position: Int): Fragment {
        return StageFragment(currentStages[position])
    }

    override fun getCount(): Int {
        return currentStages.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItemId(position: Int): Long {
        return baseId + position
    }

    fun notifyChange(newStages: List<Stage>) {
        baseId += count + currentStages.size
        currentStages = newStages
        notifyDataSetChanged()
    }
}
