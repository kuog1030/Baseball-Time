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
        val binding = DialogPinchBinding.inflate(inflater, container, false)
        val gameViewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)


        val adapter = PinchAdapter(PinchAdapter.OnClickListener{ player, position ->
            gameViewModel.pinch(player, position)
            dismiss()
        })

        binding.recyclerPinchPlayers.adapter = adapter
        adapter.submitList(gameViewModel.homeBench)

        return binding.root
    }
}