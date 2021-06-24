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
import com.gillian.baseball.databinding.PagerLoginSearchBinding
import com.gillian.baseball.login.UserManager


class PagerLoginSearch : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = PagerLoginSearchBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireParentFragment()).get(LoginFirstViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        // search player -> register player -> fetch Team -> navigate
        viewModel.playerList.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (it.isNotEmpty()) viewModel.player.value = it[0]
            }
        })

        viewModel.signUpUserFromRegister.observe(viewLifecycleOwner, Observer {
            it?.let {
                // user manager team id, player id will be added first -> then user id (repository)
                viewModel.registerPlayer()
            }
        })

        viewModel.proceedFetchTeam.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.fetchTeam()
            }
        })

        viewModel.navigateFromRegister.observe(viewLifecycleOwner, Observer {
            it?.let{
                UserManager.team = it
                findNavController().navigate(NavigationDirections.navigationToTeam())
                viewModel.fromRegisterNavigated()
            }
        })


        return binding.root
    }
}