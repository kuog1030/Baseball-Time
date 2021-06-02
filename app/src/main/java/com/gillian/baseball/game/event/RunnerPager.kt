package com.gillian.baseball.game.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.databinding.PagerRunnerBinding

class RunnerPager(val page : String, val atBase: AtBase) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = PagerRunnerBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireParentFragment()).get(EventViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.atBase = atBase
        binding.page = page


        return binding.root
    }
}