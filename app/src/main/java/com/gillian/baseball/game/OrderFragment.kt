package com.gillian.baseball.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.data.Game
import com.gillian.baseball.databinding.FragmentOrderBinding
import com.gillian.baseball.ext.getVmFactory

class OrderFragment : Fragment() {


    private val viewModel by viewModels<OrderViewModel> {getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentOrderBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToGame.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(OrderFragmentDirections.actionOrderFragmentToGameFragment(it))
                viewModel.onGameNavigated()
            }
        })

        return binding.root
    }
}