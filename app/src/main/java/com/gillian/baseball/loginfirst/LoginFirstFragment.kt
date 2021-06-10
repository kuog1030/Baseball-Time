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

    val args: LoginFirstFragmentArgs by navArgs()
    private val viewModel by viewModels<LoginFirstViewModel> { getVmFactory(args.newUser) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginFirstBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.viewpagerFirstLogin.adapter = LoginFirstAdapter(childFragmentManager)
        binding.tabsFirstLogin.setupWithViewPager(binding.viewpagerFirstLogin)

        // two way to sign up
        // 1. new create
        // sign up user -> init team and player
        // repository.signUpUser -> repository.initTeamAndPlayer
        //   ( user id )                  ( team, team id, player id )

        // 2. register existed player
        // sign up user from register -> registerPlayer() -> navigate from register
        //               repository.signUpUser -> repository.registerPlayer
        // (team, player id)    ( user id )


        // From pager search and register
        viewModel.playerList.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (it.isNotEmpty()) {
                    viewModel.player.value = it[0]
                }
            }
        })

        // register player -> fetch Team -> navigate
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
                Log.i("gillian67", "fetch team success ${UserManager.team}")
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
                findNavController().navigate(NavigationDirections.navigationToTeam())
                viewModel.onTeamNavigated()
            }
        })



        return  binding.root
    }

}