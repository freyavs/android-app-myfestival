package com.example.myfestival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfestival.adapters.NewsfeedAdapter
import com.example.myfestival.adapters.StageAdapter
import com.example.myfestival.data.NewsfeedItem
import com.example.myfestival.databinding.NewsfeedFragmentBinding
import com.example.myfestival.databinding.StageFragmentBinding

/**
 * A simple [Fragment] subclass.
 */
class NewsfeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : NewsfeedFragmentBinding = NewsfeedFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root



        val logo = ResourcesCompat.getDrawable(resources, R.mipmap.bakfietslogo, null)
        val img = ResourcesCompat.getDrawable(resources, R.mipmap.uberdope, null)

        val item1 = NewsfeedItem(R.mipmap.bakfietslogo, "Bakfiets","16:40", R.mipmap.uberdope, "Vanavond aan de dope met Uberdope")
        val item2 = NewsfeedItem(R.mipmap.bakfietslogo, "Bakfiets","16:40", R.mipmap.martinipost, "Even weg van uw kinderen? Kom genieten aan onze martinistand")
        val item3 = NewsfeedItem(R.mipmap.bakfietslogo, "Bakfiets","16:40", R.mipmap.randanimatie, "Uw kinderen even beu? Laat ze achter bij onze volkspelen")

        binding.stageRecycler.adapter = NewsfeedAdapter(listOf(item1, item2, item3))
        binding.stageRecycler.layoutManager = LinearLayoutManager(this.context)
        binding.stageRecycler.setHasFixedSize(true)

        //todo(!! wegwerken ...)
        //val decorator = DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL)
        //decorator.setDrawable(ContextCompat.getDrawable(this.context!!, R.drawable.spacer)!!)

        //binding.stageRecycler.addItemDecoration(decorator)

        return binding.root
    }

}
