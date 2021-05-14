package com.gillian.baseball.game.batting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.databinding.FragmentBattingBinding
import com.gillian.baseball.factory.ViewModelFactory
import com.gillian.baseball.game.dialog.EventDialog

class BattingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBattingBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory((requireContext().applicationContext as BaseballApplication).repository)
        ).get(BattingViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        Log.i("gillian", "in batting fragment viewmodel is $viewModel")

        viewModel.navigateToEvent.observe(viewLifecycleOwner, Observer {
            it?.let{

                for(i in it) {
                    Log.i("gillian", "at base list is ${i}")
                }

                val eventDialog = EventDialog(it, viewModel.hitterEvent)
                eventDialog.show(childFragmentManager, "Success")
                //findNavController().navigate(NavigationDirections.navigationToEventDialog(it))
                viewModel.onEventNavigated()
            }
        })

        viewModel.navigateToRunner.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.onEventNavigated()
            }
        })


        return binding.root
    }
}