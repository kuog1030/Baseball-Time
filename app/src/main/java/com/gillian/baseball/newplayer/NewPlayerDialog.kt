package com.gillian.baseball.newplayer

import android.content.Intent
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

class NewPlayerDialog(val fromTeamFragment: Boolean = false) : BottomSheetDialogFragment() {

    private val viewModel by viewModels<NewPlayerViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = DialogNewPlayerBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


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
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply{
            type = "image/*"
        }
        startActivityForResult(intent, 1)

    }

}

//                Toast.makeText(context, "新增球員成功", Toast.LENGTH_SHORT).show()
//                val timer = object : CountDownTimer(500, 500){
//                    override fun onTick(millisUntilFinished: Long) {
//                    }
//                    override fun onFinish() {
//                        dismiss()
//                    }
//                }
//                timer.start()