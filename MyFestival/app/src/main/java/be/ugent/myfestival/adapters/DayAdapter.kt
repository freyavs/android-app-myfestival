package be.ugent.myfestival.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import be.ugent.myfestival.StageFragment
import be.ugent.myfestival.models.Stage


class DayAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    private var currentStages = emptyList<Stage>()
    /*
     * Inhoud van dayadapter gebaseerd op: https://stackoverflow.com/questions/10396321/remove-fragment-page-from-viewpager-in-android
     */

    var baseId: Long = 0

    override fun getItem(position: Int): Fragment {
        return StageFragment(currentStages[position], position == 0, position == currentStages.size - 1)
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

    //omdat we willen dat er enkel nieuwe fragmenten in de pageviewer zitten moeten we het id shiften
    //en een nieuw id geven aan de stages. Anders blijven de vorige staan
    fun notifyChange(newStages: List<Stage>) {
        baseId += count + currentStages.size
        currentStages = newStages
        notifyDataSetChanged()
    }
}
