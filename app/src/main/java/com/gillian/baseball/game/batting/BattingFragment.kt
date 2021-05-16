package com.gillian.baseball.game.batting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.MainActivity
import com.gillian.baseball.databinding.FragmentBattingBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.factory.GameViewModelFactory
import com.gillian.baseball.factory.ViewModelFactory
import com.gillian.baseball.game.GameFragmentArgs
import com.gillian.baseball.game.GameViewModel
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

        //val gameViewModel by viewModels<GameViewModel> ({activity as MainActivity }) {getVmFactory(GameFragmentArgs.fromBundle(requireArguments()).preGame)}
        //val gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)
        //Log.i("gillian", "in batting $gameViewModel")
        //viewModel.lineup = gameViewModel.game.value!!.home.lineUp

        val gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)
        Log.i("gillian", "in batting : game View Model $gameViewModel")




        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        //Log.i("gillian", "in batting : batting viewmodel is $viewModel")

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