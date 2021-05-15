package com.gillian.baseball.game.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.databinding.DialogOnBaseBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.game.batting.BattingViewModel

class OnBaseDialog(val onClickPlayer: Int, val argsBaseList: Array<EventPlayer?>) : AppCompatDialogFragment() {

    private val viewModel by viewModels<OnBaseDialogViewModel> {getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DialogOnBaseBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.atBase = AtBase(base = onClickPlayer, player = argsBaseList[onClickPlayer]!!)
        viewModel.player = argsBaseList[onClickPlayer]
        val battingViewModel = ViewModelProvider(requireParentFragment()).get(BattingViewModel::class.java)

        viewModel.proceed.observe(viewLifecycleOwner, Observer {
            it?.let{
                battingViewModel.advanceBase(onClickPlayer)
                viewModel.onProceedDone()
            }
        })

        viewModel.onBaseOut.observe(viewLifecycleOwner, Observer {
            it?.let{
                battingViewModel.onBaseOut(onClickPlayer)
                viewModel.onOutDone()
            }
        })

        viewModel.dismiss.observe(viewLifecycleOwner, Observer {
            dismiss()
        })

        return binding.root
    }
}
