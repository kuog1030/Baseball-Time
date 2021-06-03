package com.gillian.baseball.game.onbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.R
import com.gillian.baseball.data.OnBaseInfo
import com.gillian.baseball.databinding.DialogOnBaseBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.game.GameViewModel


class OnBaseDialog(val onBaseInfo: OnBaseInfo) : AppCompatDialogFragment() {

    private val viewModel by viewModels<OnBaseDialogViewModel> {getVmFactory(onBaseInfo) }
    private val onClickPlayer = onBaseInfo.onClickPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.GameDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DialogOnBaseBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.onBaseInfo = onBaseInfo


        val gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)

        viewModel.proceed.observe(viewLifecycleOwner, Observer {
            it?.let{
                gameViewModel.advanceBase(onClickPlayer)
                viewModel.onProceedDone()
                dismiss()
            }
        })

        viewModel.onBaseOut.observe(viewLifecycleOwner, Observer {
            it?.let{
                gameViewModel.onBaseOut(listOf(onClickPlayer), it)
                viewModel.onOutDone()
                dismiss()
            }
        })

        binding.buttonOnBaseDismiss.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}
