package com.gillian.baseball.loginfirst

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.databinding.FragmentLoginFirstBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.login.UserManager

class LoginFirstFragment : Fragment() {

    private val args: LoginFirstFragmentArgs by navArgs()
    private val viewModel by viewModels<LoginFirstViewModel> { getVmFactory(args.newUser) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginFirstBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.viewpagerFirstLogin.adapter = LoginFirstAdapter(childFragmentManager)
        binding.tabsFirstLogin.setupWithViewPager(binding.viewpagerFirstLogin)

        return binding.root
    }

}