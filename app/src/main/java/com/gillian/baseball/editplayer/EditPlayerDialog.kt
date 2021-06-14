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
import com.gillian.baseball.NavigationDirections
import com.gillian.baseball.data.Player
import com.gillian.baseball.databinding.DialogEditPlayerBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.newplayer.NewPlayerViewModel
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

        binding.newPhoto = false

        binding.imageEditPlayerPhoto.setOnClickListener{
            pickImageFromGallery()
        }

        binding.imageEditPlayerNewPhoto.setOnClickListener{
            pickImageFromGallery()
        }

        viewModel.photoUrl.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (viewModel.proceedToSave.value!!) {
                    Log.i("gillian", "proceed to save")
                    viewModel.updatePlayer()
                }
            }
        })

        viewModel.dismissDialog.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (it) {
                    ViewModelProvider(requireParentFragment()).get(StatPlayerViewModel::class.java).refresh(viewModel.needStatRefresh)
                    ViewModelProvider(requireActivity()).get(TeamViewModel::class.java).refresh()
                }
                dismiss()
                viewModel.onDialogDismiss()
            }
        })


        viewModel.navigateToTeam.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                ViewModelProvider(requireActivity()).get(TeamViewModel::class.java).refresh()
                findNavController().navigate(StatPlayerFragmentDirections.actionStatPlayerFragmentToTeamFragment())
                viewModel.onTeamNavigated()
            }
        })

        return binding.root
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply{
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_IMAGE_OPEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK && data != null) {
            binding.newPhoto = true
            binding.imageEditPlayerNewPhoto.setImageURI(data.data)
            viewModel.readyToSentPhoto.value = data.data
        }
    }

}