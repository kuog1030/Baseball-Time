package com.gillian.baseball.statistics.statplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.databinding.FragmentStatPlayerBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.newplayer.REQUEST_IMAGE_OPEN
import com.gillian.baseball.views.HitterBoxAdapter
import com.gillian.baseball.views.PitcherBoxAdapter

const val REQUEST_IMAGE_OPEN = 1

class StatPlayerFragment : Fragment() {

    private val viewModel by viewModels<StatPlayerViewModel> { getVmFactory() }
    private lateinit var binding : FragmentStatPlayerBinding
    private val args: StatPlayerFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentStatPlayerBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getPlayerStat(args.playerId)

        viewModel.player.observe(viewLifecycleOwner, Observer {
            it?.let{
                binding.hitStat = it.hitStat
                binding.pitchStat = it.pitchStat
                viewModel.updateMoreHitStat()
            }
        })

        binding.imageStatPlayer.setOnClickListener{
            if (viewModel.editable.value!!) {
                pickImageFromGallery()
            }
        }


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
            val fullPhotoUri  = data.data
            binding.imageStatPlayer.setImageURI(data.data)
        }
    }

}