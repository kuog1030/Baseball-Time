package com.gillian.baseball.game.event

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gillian.baseball.data.AtBase

class EventAdapter(
        fragmentManager: FragmentManager,
        private val isSafe: Boolean,
        private val atBaseList: List<AtBase>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        // the order will be hitter -> last runner -...-> first runner -> end page
        // which means the runner pager goes backward ( third base show before second base)
        return when (position) {
            0 -> {
                if (isSafe) {
                    HitterPager("1/$count", atBaseList.last())
                } else {
                    OutPager("1/$count", atBaseList[position])
                }
            }
            (count - 1) -> EndPager("$count/$count")
            else -> RunnerPager("${position + 1}/$count", atBaseList[count - 1 - position])
        }

    }

    override fun getCount(): Int {
        return (atBaseList.size + 1)
    }
}