package com.gillian.baseball.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.MainActivity
import com.gillian.baseball.databinding.FragmentGameBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.game.dialog.EventDialog
import com.gillian.baseball.game.onbase.OnBaseDialog
import com.gillian.baseball.game.pinch.PinchDialog

class GameFragment : Fragment() {

   private val viewModel by viewModels<GameViewModel> {getVmFactory(GameFragmentArgs.fromBundle(requireArguments()).preGame) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        (activity as MainActivity).setBottomNavigation(false)


        viewModel.navigateToEvent.observe(viewLifecycleOwner, Observer {
            it?.let{
                val eventDialog = EventDialog(it)
                eventDialog.show(childFragmentManager, "Success")
                viewModel.onEventNavigated()
            }
        })

        viewModel.navigateToOut.observe(viewLifecycleOwner, Observer {
            it?.let{
                val eventDialog = EventDialog(it)
                eventDialog.show(childFragmentManager, "Out")
                viewModel.onOutNavigated()
            }
        })

        viewModel.navigateToOnBase.observe(viewLifecycleOwner, Observer {
            it?.let {
                val onBaseDialog = OnBaseDialog(it)
                onBaseDialog.show(childFragmentManager, "OnBase")
                viewModel.onOnBaseNavigated()
            }
        })

        viewModel.navigateToFinal.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(GameFragmentDirections.actionGameFragmentToFinalFragment(it))
                viewModel.onFinalNavigated()
            }
        })

        viewModel.navigateToPinch.observe(viewLifecycleOwner, Observer {
            it?.let{
                val pinchDialog = PinchDialog(viewModel.isTop == viewModel.isHome)
                pinchDialog.show(childFragmentManager, "pinch")
                viewModel.onPinchNavigated()
            }
        })


        return binding.root
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setBottomNavigation(true)
    }
}