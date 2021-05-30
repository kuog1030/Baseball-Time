package com.gillian.baseball.broadcast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.databinding.FragmentBroadcastBinding
import com.gillian.baseball.ext.getVmFactory

class BroadcastFragment : Fragment() {

    val args: BroadcastFragmentArgs by navArgs()
    private val viewModel by viewModels<BroadcastViewModel> {getVmFactory(args.game) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentBroadcastBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        Log.i("gillian", "view model liveevent1 ${viewModel.liveEvents}")
        viewModel
        val adapter = BroadcastAdapter()

        binding.recyclerLive.adapter = adapter
        binding.viewModel = viewModel


        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            Log.i("gillian", "view model liveevent4 ${viewModel.liveEvents}")
            it?.let{
                Log.i("gillian", "i get new event size = ${it.size} event ${it}")
                adapter.notifyDataSetChanged()
            }
        })




        return binding.root
    }
}