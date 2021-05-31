package com.gillian.baseball.loginflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gillian.baseball.databinding.FragmentLoginSearchBinding
import com.gillian.baseball.ext.getVmFactory

class LoginSearchFragment : Fragment() {

    private val viewModel by viewModels<LoginSearchViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginSearchBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        return  binding.root
    }

}