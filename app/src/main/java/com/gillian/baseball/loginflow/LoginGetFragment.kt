package com.gillian.baseball.loginflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.databinding.FragmentLoginGetBinding
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.team.pager.TeammateAdapter

class LoginGetFragment : Fragment() {

    val args: LoginGetFragmentArgs by navArgs()
    private val viewModel by viewModels<LoginGetViewModel> { getVmFactory(args.newUser) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginGetBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.team = args.myTeam
        viewModel.getTeamPlayer(args.myTeam.id)

        binding.recyclerGetTeammate.adapter = TeammateAdapter(TeammateAdapter.OnClickListener { player ->
            binding.textGetChosen.text = player.name
            viewModel.setTeamAndPlayer(myTeam = args.myTeam, myPlayer = player)
        })

        viewModel.signUpUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                // user manager team id, player id will be added first -> then user id (repository)
                viewModel.registerPlayer()
            }
        })

        viewModel.navigateToTeam.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigationToTeam())
                viewModel.onTeamNavigated()
            }
        })



        return binding.root
    }

}