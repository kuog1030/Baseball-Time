package com.gillian.baseball.game.dialog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class EventDialogAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HitterFragment()
            (count-1) -> EventEndFragment()
            else -> RunnerFragment()
        }
    }

    override fun getCount(): Int = 4
}