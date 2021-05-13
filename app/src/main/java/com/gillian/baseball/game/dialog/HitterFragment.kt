package com.gillian.baseball.game.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gillian.baseball.databinding.FragmentHitterBinding
import com.gillian.baseball.ext.getVmFactory

class HitterFragment : Fragment() {

    val viewModel by viewModels<HitterViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHitterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        return binding.root
    }

}