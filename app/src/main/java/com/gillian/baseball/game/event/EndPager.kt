package com.gillian.baseball.game.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.databinding.PagerEndBinding

class EndPager (val page: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = PagerEndBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireParentFragment()).get(EventViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.page = page
        binding.viewModel = viewModel


        return binding.root
    }
}