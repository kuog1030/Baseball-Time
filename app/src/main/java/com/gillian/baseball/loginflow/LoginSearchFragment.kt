package com.gillian.baseball.loginflow

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
import com.gillian.baseball.data.User
import com.gillian.baseball.databinding.FragmentLoginSearchBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.team.pager.TeammateAdapter

class LoginSearchFragment : Fragment() {

    val args: LoginSearchFragmentArgs by navArgs()
    private val viewModel by viewModels<LoginSearchViewModel> { getVmFactory(args.newUser) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginSearchBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerSearchTeams.adapter = TeammateAdapter(TeammateAdapter.OnClickListener{ player ->
            Log.i("gillian", "player clicked is $player")
            viewModel.player = player
        })

        viewModel.signUpUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                // user manager team id, player id will be added first -> then user id (repository)
                viewModel.registerPlayer()
            }
        })

        viewModel.navigateToCreate.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(LoginSearchFragmentDirections.actionSearchToCreate(args.newUser))
                viewModel.onCreateNavigated()
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