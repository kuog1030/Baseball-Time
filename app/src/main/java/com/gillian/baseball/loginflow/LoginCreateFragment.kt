package com.gillian.baseball.loginflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.databinding.FragmentLoginCreateBinding
import com.gillian.baseball.ext.getVmFactory

class LoginCreateFragment : Fragment() {

    val args: LoginCreateFragmentArgs by navArgs()
    private val viewModel by viewModels<LoginCreateViewModel> { getVmFactory(args.newUser) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginCreateBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.signUpUser.observe(viewLifecycleOwner, Observer {
            it?.let{
                // usermanager user id will be added here
                viewModel.navigateToTeam()
            }
        })

        viewModel.navigateToTeamOrOrder.observe(viewLifecycleOwner, Observer {
            it?.let{
                // usermanager team id and player id will be added here
                if(viewModel.goToTeam) {
                    findNavController().navigate(NavigationDirections.navigationToTeam())
                } else {
                    findNavController().navigate(NavigationDirections.navigationToOrder(null))
                }
                viewModel.onNavigatedDone()
            }
        })


        return  binding.root
    }

}