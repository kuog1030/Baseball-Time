package com.gillian.baseball.loginfirst

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.databinding.PagerLoginCreateBinding


class PagerLoginCreate : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = PagerLoginCreateBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireParentFragment()).get(LoginFirstViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        // From pager create
        viewModel.signUpUser.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.initTeamAndPlayer()
            }
        })

        viewModel.navigateToTeam.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(NavigationDirections.navigationToTeam())
                viewModel.onTeamNavigated()
            }
        })


        return binding.root
    }
}