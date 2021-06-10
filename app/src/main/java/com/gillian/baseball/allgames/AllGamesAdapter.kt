package com.gillian.baseball.allgames

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gillian.baseball.allgames.schedule.SchedulePager
import com.gillian.baseball.allgames.scores.ScoresPager


class AllGamesAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titleList = listOf("未來比賽", "已結束")

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> SchedulePager()
            else -> ScoresPager()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}