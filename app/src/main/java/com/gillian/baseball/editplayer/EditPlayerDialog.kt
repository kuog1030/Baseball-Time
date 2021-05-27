package com.gillian.baseball.editplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gillian.baseball.databinding.DialogEditPlayerBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.newplayer.NewPlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


const val REQUEST_IMAGE_OPEN = 1

class EditPlayerDialog() : BottomSheetDialogFragment() {
//
//    private val viewModel by viewModels<NewPlayerViewModel> { getVmFactory() }
//    private lateinit var binding: DialogEditPlayerBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View? {
//        binding = DialogEditPlayerBinding.inflate(inflater, container, false)
//
//        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel = viewModel
//
//        binding.imageEditPlayerPhoto.setOnClickListener{
//            pickImageFromGallery()
//        }
//
//        return binding.root
//    }
//
//    private fun pickImageFromGallery() {
//        val intent = Intent(Intent.ACTION_PICK).apply{
//            type = "image/*"
//        }
//        startActivityForResult(intent, REQUEST_IMAGE_OPEN)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK && data != null) {
//            val fullPhotoUri  = data.data
//            binding.imageNewPlayerPhoto.setImageURI(data.data)
//            Log.i("gillian", "ready to send")
//            viewModel.uploadPhoto(fullPhotoUri!!)
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.i("gillian", "new player dialog on stop")
//    }

}