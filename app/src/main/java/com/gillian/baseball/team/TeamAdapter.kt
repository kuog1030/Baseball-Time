package com.gillian.baseball.team

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gillian.baseball.R
import com.gillian.baseball.team.pager.RankPager
import com.gillian.baseball.team.pager.TeammatePager
import com.gillian.baseball.util.Util.getString

class TeamAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titleList = listOf(getString(R.string.team_pager_all_player), getString(R.string.team_pager_team))

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> TeammatePager()
            else -> RankPager()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}