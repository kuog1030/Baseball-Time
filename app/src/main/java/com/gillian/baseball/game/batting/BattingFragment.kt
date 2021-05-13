package com.gillian.baseball.game.batting

import android.os.Bundle
import android.util.EventLog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.databinding.FragmentBattingBinding
import com.gillian.baseball.ext.getVmFactory
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

        viewModel.navigateToHitter.observe(viewLifecycleOwner, Observer {
            it?.let{
                val eventDialog = EventDialog(it)
                eventDialog.show(childFragmentManager, "Success")
                //findNavController().navigate(NavigationDirections.navigationToEventDialog(it))
                viewModel.onHitterNavigated()
            }
        })

        viewModel.navigateToRunner.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.onHitterNavigated()
            }
        })


        return binding.root
    }
}