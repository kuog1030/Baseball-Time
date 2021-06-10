package com.gillian.baseball.game.pinch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.R
import com.gillian.baseball.databinding.DialogPinchBinding
import com.gillian.baseball.ext.getVmFactory
import com.gillian.baseball.game.GameViewModel
import com.gillian.baseball.game.order.PinchAdapter
import com.gillian.baseball.login.LoginViewModel

class PinchDialog(private val isDefense: Boolean) : AppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.GameDialog)
    }

    private val viewModel by viewModels<PinchViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DialogPinchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.setToggleText(isDefense)

        val gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)

        binding.gameTitle = gameViewModel.game.value?.title

        val benchAdapter = PinchAdapter(PinchAdapter.OnClickListener{ player, position ->
            if (isDefense) {
                gameViewModel.nextPitcher(player, position)
            } else {
                gameViewModel.pinch(player, position)
            }
            dismiss()
        })

        viewModel.dismissDialog.observe(viewLifecycleOwner, Observer {
            it?.let{
                dismiss()
                viewModel.onDialogDismiss()
            }
        })


        binding.recyclerPinchPlayer.adapter = benchAdapter
        benchAdapter.submitList(gameViewModel.myBench)

        return binding.root
    }
}