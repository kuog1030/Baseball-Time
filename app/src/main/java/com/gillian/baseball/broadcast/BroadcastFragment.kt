package com.gillian.baseball.broadcast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gillian.baseball.databinding.FragmentBroadcastBinding
import com.gillian.baseball.ext.getVmFactory

class BroadcastFragment : Fragment() {

    private val viewModel by viewModels<BroadcastViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentBroadcastBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        Log.i("gillian", "view model liveevent1 ${viewModel.liveEvents}")
        viewModel
        val adapter = BroadcastAdapter()

        binding.recyclerLive.adapter = adapter

        Log.i("gillian", "view model liveevent2 ${viewModel.liveEvents}")
        binding.viewModel = viewModel
        Log.i("gillian", "view model liveevent3 ${viewModel.liveEvents}")


        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            Log.i("gillian", "view model liveevent4 ${viewModel.liveEvents}")
            it?.let{

                Log.i("gillian", "i get new event size = ${it.size} event ${it}")
                adapter.notifyDataSetChanged()
//                adapter.submitList(it)
//                adapter.notifyDataSetChanged()
            }
        })




        return binding.root
    }
}