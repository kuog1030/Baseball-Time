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
import com.gillian.baseball.databinding.FragmentLoginSearchBinding
import com.gillian.baseball.ext.getVmFactory

class LoginSearchFragment : Fragment() {

    val args: LoginSearchFragmentArgs by navArgs()
    private val viewModel by viewModels<LoginFirstViewModel> { getVmFactory(args.newUser) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginSearchBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.viewpagerFirstLogin.adapter = LoginFirstAdapter(childFragmentManager)
        binding.tabsFirstLogin.setupWithViewPager(binding.viewpagerFirstLogin)


        // From pager search and register
        viewModel.playerList.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (it.isNotEmpty()) {
                    viewModel.player.value = it[0]
                }
            }
        })

        viewModel.signUpUserFromRegister.observe(viewLifecycleOwner, Observer {
            it?.let {
                // user manager team id, player id will be added first -> then user id (repository)
                viewModel.registerPlayer()
            }
        })

        viewModel.navigateFromRegister.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(NavigationDirections.navigationToTeam())
                viewModel.fromRegisterNavigated()
            }
        })


        // From pager create
        viewModel.signUpUser.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.initTeamAndPlayer()
            }
        })


        viewModel.navigateToTeam.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(LoginSearchFragmentDirections.actionSearchToCreate(args.newUser))
                viewModel.onTeamNavigated()
            }
        })



        return  binding.root
    }

}