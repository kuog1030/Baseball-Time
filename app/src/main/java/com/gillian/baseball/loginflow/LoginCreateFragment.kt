package com.gillian.baseball.loginflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gillian.baseball.databinding.FragmentLoginCreateBinding
import com.gillian.baseball.ext.getVmFactory

class LoginCreateFragment : Fragment() {

    private val viewModel by viewModels<LoginCreateViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginCreateBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return  binding.root
    }

}