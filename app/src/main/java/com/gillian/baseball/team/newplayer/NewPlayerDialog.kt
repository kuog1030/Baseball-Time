package com.gillian.baseball.team.newplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gillian.baseball.databinding.DialogNewPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewPlayerDialog  : BottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = DialogNewPlayerBinding.inflate(inflater, container, false)





        return binding.root
    }
}