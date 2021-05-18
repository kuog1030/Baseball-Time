package com.gillian.baseball.game.finalgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gillian.baseball.databinding.FragmentFinalBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.views.BoxAdapter


class FinalFragment : Fragment() {

    private val viewModel by viewModels<FinalViewModel> {getVmFactory(FinalFragmentArgs.fromBundle(requireArguments()).endGame)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentFinalBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        val boxAdapter = BoxAdapter()
        binding.recyclerBox.adapter = boxAdapter

        viewModel.submitAdapter.observe(viewLifecycleOwner, Observer {
            it?.let{
                boxAdapter.submitList(it)
            }
        })


        return binding.root
    }
}