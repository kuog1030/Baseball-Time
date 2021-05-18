package com.gillian.baseball.firstlogin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.databinding.PagerCreatePlayerBinding

class PagerCreatePlayer : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = PagerCreatePlayerBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(requireParentFragment()).get(FirstLoginViewModel::class.java)
        binding.viewModel = viewModel

        Log.i("gillian", "in create player viewmodel $viewModel")

        return binding.root
    }
}