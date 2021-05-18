package com.gillian.baseball.firstlogin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.R
import com.gillian.baseball.databinding.FragmentFirstLoginBinding
import com.gillian.baseball.ext.getVmFactory

class FirstLoginFragment : Fragment() {

    //private val viewModel: FirstLoginViewModel by lazy {ViewModelProvider(this).get(FirstLoginViewModel::class.java)}
    private val viewModel by viewModels<FirstLoginViewModel> {getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentFirstLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        //val viewModel = ViewModelProvider(this).get(FirstLoginViewModel::class.java)
        Log.i("gillian", "in fragment $viewModel")

        binding.viewpagerFirstLogin.adapter = FirstLoginAdapter(childFragmentManager)
        binding.tabsFirstLogin.setupWithViewPager(binding.viewpagerFirstLogin)
        binding.viewpagerFirstLoginArrow.setArrowIndicatorRes(R.drawable.ic_baseline_arrow_backward_24, R.drawable.ic_baseline_arrow_forward_24)
        binding.viewpagerFirstLoginArrow.bind(binding.viewpagerFirstLogin)

        viewModel.navigateToTeam.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(NavigationDirections.navigationToTeam())
        })


        return binding.root
    }
}