package com.gillian.baseball.game.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.databinding.PagerHitterBinding

class HitterPager(val page : String, val atBase: AtBase) : Fragment() {

    //val viewModel by viewModels<HitterViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = PagerHitterBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireParentFragment()).get(EventViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.page = page


        return binding.root
    }

}