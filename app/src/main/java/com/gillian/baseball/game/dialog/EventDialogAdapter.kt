package com.gillian.baseball.game.dialog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gillian.baseball.data.AtBase
import com.gillian.baseball.data.EventPlayer

class EventDialogAdapter(fragmentManager: FragmentManager, val atBaseList: List<AtBase>) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HitterFragment("1/$count", atBaseList[position])
            (count-1) -> EventEndFragment()
            else -> RunnerFragment("${position+1}/$count", atBaseList[position])
        }
    }

    override fun getCount(): Int {
        return (atBaseList.size +1 )
    }
}