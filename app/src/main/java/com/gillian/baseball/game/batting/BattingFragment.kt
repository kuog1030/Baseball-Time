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
                val eventDialog = EventDialog(it, true, viewModel.hitterEvent)
                eventDialog.show(childFragmentManager, "Success")
                Log.i("gillian", "navigate to event $eventDialog")
                //findNavController().navigate(NavigationDirections.navigationToEventDialog(it))
                viewModel.onEventNavigated()
            }
        })

        viewModel.navigateToOut.observe(viewLifecycleOwner, Observer {
            it?.let{
                val eventDialog = EventDialog(it, false, viewModel.hitterEvent)
                Log.i("gillian", "navigate to out $eventDialog")
                eventDialog.show(childFragmentManager, "Success")
                viewModel.onOutNavigated()
            }
        })

        viewModel.navigateToRunner.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.onEventNavigated()
            }
        })

        viewModel.updateRunner.observe(viewLifecycleOwner, Observer {
            it?.let{
                binding.textBattingFirst.text = viewModel.baseList[1]?.name ?: ""
                binding.textBattingSecond.text = viewModel.baseList[2]?.name ?: ""
                binding.textBattingThird.text = viewModel.baseList[3]?.name ?: ""
                viewModel.onUpdateRunnerDone()
            }
        })


        return binding.root
    }
}