package com.gillian.baseball.allgames

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gillian.baseball.R
import com.gillian.baseball.allgames.schedule.SchedulePager
import com.gillian.baseball.allgames.scores.ScoresPager
import com.gillian.baseball.util.Util


class AllGamesAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titleList = listOf(Util.getString(R.string.game_pager_schedule), Util.getString(R.string.game_pager_score))

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