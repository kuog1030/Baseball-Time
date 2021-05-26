package com.gillian.baseball.finalgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gillian.baseball.databinding.FragmentFinalBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.finalgame.FinalFragmentArgs
import com.gillian.baseball.views.BoxAdapter


class FinalFragment : Fragment() {

    private val viewModel by viewModels<FinalViewModel> {getVmFactory(FinalFragmentArgs.fromBundle(requireArguments()).endGame)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentFinalBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        val boxAdapter = BoxAdapter()
        binding.recyclerFinalBox.adapter = boxAdapter

        viewModel.submitAdapter.observe(viewLifecycleOwner, Observer {
            it?.let{
                boxAdapter.submitList(it)
            }
        })

        viewModel.myStat.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.updateHitStat()
                viewModel.updatePitchStat()
            }
        })


        return binding.root
    }
}