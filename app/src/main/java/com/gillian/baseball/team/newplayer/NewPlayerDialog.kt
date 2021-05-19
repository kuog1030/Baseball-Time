package com.gillian.baseball.team.newplayer

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.gillian.baseball.R
import com.gillian.baseball.databinding.DialogNewPlayerBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.team.TeamFragment
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

                try {
                    if (fromTeamFragment && viewModel.needRefresh) {
                        ViewModelProvider(requireParentFragment()).get(TeamViewModel::class.java)
                            .refresh()
                    }
                } finally {
                    dismiss()
                    viewModel.onDialogDismiss()
                }
            }
        })


        return binding.root
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