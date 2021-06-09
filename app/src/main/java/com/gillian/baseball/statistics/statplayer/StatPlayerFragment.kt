package com.gillian.baseball.statistics.statplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.databinding.FragmentStatPlayerBinding
import com.gillian.baseball.editplayer.EditPlayerDialog
import com.gillian.baseball.ext.getVmFactory

const val REQUEST_IMAGE_OPEN = 1

class StatPlayerFragment : Fragment() {

    private val viewModel by viewModels<StatPlayerViewModel> { getVmFactory() }
    private lateinit var binding : FragmentStatPlayerBinding
    private val args: StatPlayerFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentStatPlayerBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.fetchPlayerStat(args.playerId)

        binding.buttonStatPlayerLeave.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.player.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (viewModel.isInit) {
                    binding.hitStat = it.hitStat
                    binding.pitchStat = it.pitchStat
                    viewModel.updateMoreHitStat()
                }
            }
        })

        viewModel.navigateToEdit.observe(viewLifecycleOwner, Observer {
            it?.let{
                val editPlayerDialog = EditPlayerDialog(it)
                editPlayerDialog.show(childFragmentManager, "edit")
                viewModel.onEditNavigated()
            }
        })

//        viewModel.navigateToTeam.observe(viewLifecycleOwner, Observer {
//            it?.let{
//                findNavController().navigate(NavigationDirections.navigationToTeam())
//            }
//        })

        return binding.root
    }
}