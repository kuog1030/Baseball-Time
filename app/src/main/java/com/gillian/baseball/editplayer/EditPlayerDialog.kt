package com.gillian.baseball.editplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gillian.baseball.data.Player
import com.gillian.baseball.databinding.DialogEditPlayerBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.statistics.statplayer.StatPlayerFragmentDirections
import com.gillian.baseball.statistics.statplayer.StatPlayerViewModel
import com.gillian.baseball.team.TeamViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val REQUEST_IMAGE_OPEN = 1

class EditPlayerDialog(val player: Player) : BottomSheetDialogFragment() {

    private val viewModel by viewModels<EditPlayerViewModel> { getVmFactory() }
    private lateinit var binding: DialogEditPlayerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogEditPlayerBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.initOriginInfo(player)

        binding.imageEditPlayerPhoto.setOnClickListener {
            pickImageFromGallery()
        }


        viewModel.newImage.observe(viewLifecycleOwner, Observer {
            it?.let { image ->
                viewModel.updatePlayer(image)
            }
        })

        viewModel.dismissDialog.observe(viewLifecycleOwner, Observer {
            it?.let { hasChange ->
                if (hasChange) {
                    ViewModelProvider(requireParentFragment()).get(StatPlayerViewModel::class.java).refresh()
                    ViewModelProvider(requireActivity()).get(TeamViewModel::class.java).refresh()
                }
                dismiss()
                viewModel.onDialogDismiss()
            }
        })


        // delete player -> navigate to team
        viewModel.navigateToTeam.observe(viewLifecycleOwner, Observer {
            it?.let {
                ViewModelProvider(requireActivity()).get(TeamViewModel::class.java).refresh()
                findNavController().navigate(StatPlayerFragmentDirections.actionStatPlayerFragmentToTeamFragment())
                viewModel.onTeamNavigated()
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
            binding.imageEditPlayerNew.setImageURI(data.data)
            viewModel.photoToBeSent.value = data.data
        }
    }

}