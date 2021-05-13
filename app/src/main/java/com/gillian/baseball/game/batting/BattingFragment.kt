package com.gillian.baseball.game.batting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.databinding.FragmentBattingBinding
import com.gillian.baseball.ext.getVmFactory

class BattingFragment : Fragment() {

    val viewModel by viewModels<BattingViewModel> { getVmFactory() }
    //val viewModel: BattingViewModel by lazy { ViewModelProvider(this).get(BattingViewModel::class.java)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBattingBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        viewModel.navigateToHitter.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(NavigationDirections.navigationToEventDialog())
                viewModel.onHitterNavigated()
            }
        })

        viewModel.navigateToRunner.observe(viewLifecycleOwner, Observer {

        })



        return binding.root
    }
}