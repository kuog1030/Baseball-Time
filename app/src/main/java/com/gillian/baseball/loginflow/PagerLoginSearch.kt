package com.gillian.baseball.loginflow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.databinding.PagerLoginSearchBinding


class PagerLoginSearch : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = PagerLoginSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        val viewModel = ViewModelProvider(requireParentFragment()).get(LoginFirstViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }
}