package com.gillian.baseball.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gillian.baseball.databinding.FragmentTeamBinding

class TeamFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentTeamBinding.inflate(inflater, container, false)

        return binding.root
    }
}