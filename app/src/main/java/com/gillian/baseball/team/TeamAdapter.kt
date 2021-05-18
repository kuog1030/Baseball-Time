package com.gillian.baseball.team

import android.provider.Settings.Global.getString
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gillian.baseball.R
import com.gillian.baseball.team.pager.RankPager
import com.gillian.baseball.team.pager.TeammatePager

class TeamAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titleList = listOf("所有隊員", "排行榜")

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