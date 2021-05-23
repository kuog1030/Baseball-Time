package com.gillian.baseball.game.pinch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.R
import com.gillian.baseball.databinding.DialogPinchBinding
import com.gillian.baseball.game.GameViewModel
import com.gillian.baseball.game.order.PinchAdapter

class PinchDialog(private val isDefense: Boolean) : AppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.GameDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DialogPinchBinding.inflate(inflater, container, false)
        val gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)

        binding.isDefense = isDefense

        val benchAdapter = PinchAdapter(PinchAdapter.OnClickListener{ player, position ->
            if (isDefense) {
                gameViewModel.nextPitcher(player, position)
            } else {
                gameViewModel.pinch(player, position)
            }
            dismiss()
        })

        val fieldAdapter = PinchAdapter(PinchAdapter.OnClickListener{ player, position ->
            gameViewModel.pinch(player, position)
            dismiss()
        })


        binding.recyclerPinchBench.adapter = benchAdapter
        binding.recyclerPinchOnField.adapter = fieldAdapter
        benchAdapter.submitList(gameViewModel.myBench)
        fieldAdapter.submitList(gameViewModel.lineUp)

        binding.buttonPinchHitter.setOnClickListener {
            binding.recyclerPinchOnField.visibility = View.INVISIBLE
            binding.recyclerPinchBench.visibility = View.VISIBLE
        }
        binding.buttonPinchError.setOnClickListener {
            binding.recyclerPinchOnField.visibility = View.VISIBLE
            binding.recyclerPinchBench.visibility = View.INVISIBLE
        }

        return binding.root
    }
}