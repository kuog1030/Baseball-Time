package com.gillian.baseball.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gillian.baseball.databinding.FragmentTeamBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.login.UserManager
import com.gillian.baseball.newplayer.NewPlayerDialog

class TeamFragment : Fragment() {

    private val viewModel by viewModels<TeamViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTeamBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.viewpagerTeams.adapter = TeamAdapter(childFragmentManager)
        binding.tabsTeams.setupWithViewPager(binding.viewpagerTeams)

        viewModel.showNewPlayerDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                val newPlayerDialog = NewPlayerDialog(true)
                newPlayerDialog.show(childFragmentManager, "")
                viewModel.onNewPlayerDialogShowed()
            }
        })

        //TODO()有沒有更好的寫法
        viewModel.team.observe(viewLifecycleOwner, Observer {
            it?.let {
                UserManager.teamName = it.name
                viewModel.teamName.value = it.name
            }
        })

        return binding.root
    }
}