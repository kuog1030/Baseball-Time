package com.gillian.baseball.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R
import com.gillian.baseball.databinding.FragmentGameBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.game.event.EventDialog
import com.gillian.baseball.game.onbase.OnBaseDialog
import com.gillian.baseball.game.pinch.PinchDialog

class GameFragment : Fragment() {

    private val viewModel by viewModels<GameViewModel> {getVmFactory(GameFragmentArgs.fromBundle(requireArguments()).preGame) }
    private var backPressedTime = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressedCheck()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel.navigateToEvent.observe(viewLifecycleOwner, Observer {
            it?.let{
                val eventDialog = EventDialog(it)
                eventDialog.show(childFragmentManager, "Event")
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
                pinchDialog.show(childFragmentManager, "Pinch")
                viewModel.onPinchNavigated()
            }
        })


        return binding.root
    }


    private fun backPressedCheck() {
        if (backPressedTime + 2000 < System.currentTimeMillis()) {
            Toast.makeText(BaseballApplication.instance, getString(R.string.double_tap), Toast.LENGTH_SHORT).show()
        } else {
            findNavController().popBackStack()
        }
        backPressedTime = System.currentTimeMillis()
    }
}