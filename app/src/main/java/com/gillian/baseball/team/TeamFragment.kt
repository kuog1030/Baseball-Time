package com.gillian.baseball.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.databinding.FragmentTeamBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.newplayer.NewPlayerDialog

class TeamFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentTeamBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireActivity(), this.getVmFactory()).get(TeamViewModel::class.java)

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


        return binding.root
    }
}