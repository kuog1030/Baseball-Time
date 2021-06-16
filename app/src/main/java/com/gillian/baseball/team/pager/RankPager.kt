package com.gillian.baseball.team.pager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.databinding.PagerRankBinding
import com.gillian.baseball.editplayer.REQUEST_IMAGE_OPEN
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.team.TeamFragmentDirections
import com.gillian.baseball.team.TeamViewModel

class RankPager : Fragment() {

    lateinit var binding: PagerRankBinding
    lateinit var viewModel: TeamViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PagerRankBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), this.getVmFactory()).get(TeamViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = RankAdapter()
        binding.recyclerRank.adapter = adapter

        binding.imageRankTeam.setOnClickListener {
            if (viewModel.editable.value == true) {
                pickImageFromGallery()
            }
        }

        viewModel.newTeamImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.updateTeamInfo(it)
            }
        })

        viewModel.navigateToTeamStat.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(TeamFragmentDirections.actionTeamFragmentToStatTeamFragment())
                viewModel.onTeamStatNavigated()
            }
        })

        return binding.root
    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_IMAGE_OPEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK && data != null) {
            binding.imageRankTeam.setImageURI(data.data)
            viewModel.photoToBeSent.value = data.data
        }
    }

}