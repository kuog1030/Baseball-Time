package com.gillian.baseball.newplayer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.databinding.DialogNewPlayerBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.order.OrderViewModel
import com.gillian.baseball.team.TeamViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val REQUEST_IMAGE_OPEN = 1

class NewPlayerDialog(val fromTeamFragment: Boolean = false) : BottomSheetDialogFragment() {

    private val viewModel by viewModels<NewPlayerViewModel> { getVmFactory() }
    private lateinit var binding: DialogNewPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogNewPlayerBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.imageNewPlayerPhoto.setOnClickListener{
            pickImageFromGallery()
        }

        viewModel.photoUrl.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (viewModel.proceedToSave.value!!) {
                    Log.i("gillian", "upload success ma $it")
                    viewModel.createPlayer()
                }
            }
        })


        viewModel.dismissDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (viewModel.needRefresh) {
                    try{
                        if (fromTeamFragment) {
                            ViewModelProvider(requireParentFragment()).get(TeamViewModel::class.java).refresh()
                        } else {
                            ViewModelProvider(requireParentFragment()).get(OrderViewModel::class.java).refresh()
                        }
                    } finally {
                    }
                }
                dismiss()
                viewModel.onDialogDismiss()
            }
        })


        return binding.root
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply{
            type = "image/*"
            //addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, REQUEST_IMAGE_OPEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK && data != null) {
            binding.imageNewPlayerPhoto.setImageURI(data.data)
            viewModel.readyToSentPhoto.value = data.data
            Log.i("gillian", "ready to send photo is not null ${viewModel.readyToSentPhoto.value}")
        }
    }

    override fun onStop() {
        super.onStop()
        Log.i("gillian", "new player dialog on stop")
    }

}