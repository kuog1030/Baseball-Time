package com.gillian.baseball.team.pager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.data.Player
import com.gillian.baseball.databinding.PagerTeammateBinding
import com.gillian.baseball.team.TeamViewModel
import com.gillian.baseball.team.newplayer.NewPlayerDialog

class TeammatePager : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = PagerTeammateBinding.inflate(inflater, container, false)


        val viewModel = ViewModelProvider(requireParentFragment()).get(TeamViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerTeamTeammate.adapter = TeammateAdapter(TeammateAdapter.OnClickListener{ player, position ->
            Log.i("gillian", "eee do something")
        })


//        viewModel.showNewPlayerDialog.observe(viewLifecycleOwner, Observer {
//            it?.let{
//                findNavController().navigate(NavigationDirections.navigationToNewPlayer(true))
//                viewModel.onNewPlayerDialogShowed()
//            }
//        })

        return binding.root
    }
}