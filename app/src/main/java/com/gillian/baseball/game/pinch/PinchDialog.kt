package com.gillian.baseball.game.pinch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import com.gillian.baseball.data.EventPlayer
import com.gillian.baseball.databinding.DialogPinchBinding
import com.gillian.baseball.game.GameViewModel
import com.gillian.baseball.game.order.PinchAdapter

class PinchDialog : AppCompatDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var isChangingPitcher = false
        val binding = DialogPinchBinding.inflate(inflater, container, false)
        val gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)


        val benchAdapter = PinchAdapter(PinchAdapter.OnClickListener{ player, position ->
            if (isChangingPitcher) {
                gameViewModel.nextPitcher(player)
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
        benchAdapter.submitList(gameViewModel.homeBench)
        fieldAdapter.submitList(gameViewModel.lineUp)

        binding.buttonPinchPitcher.setOnClickListener {
            isChangingPitcher = true
            binding.recyclerPinchOnField.visibility = View.INVISIBLE
            binding.recyclerPinchBench.visibility = View.VISIBLE
        }
        binding.buttonPinchHitter.setOnClickListener {
            isChangingPitcher = false
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