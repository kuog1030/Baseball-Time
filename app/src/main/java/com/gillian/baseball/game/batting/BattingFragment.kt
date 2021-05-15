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
import com.gillian.baseball.game.dialog.OnBaseDialog

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
                viewModel.onEventNavigated()
            }
        })

        viewModel.navigateToOut.observe(viewLifecycleOwner, Observer {
            it?.let{
                val eventDialog = EventDialog(it, false, viewModel.hitterEvent)
                eventDialog.show(childFragmentManager, "Out")
                viewModel.onOutNavigated()
            }
        })

        viewModel.navigateToOnBase.observe(viewLifecycleOwner, Observer {
            it?.let {
                val onBaseDialog = OnBaseDialog(it, viewModel.baseList)
                onBaseDialog.show(childFragmentManager, "OnBase")
                viewModel.onOnBaseNavigated()
            }
        })



        return binding.root
    }
}