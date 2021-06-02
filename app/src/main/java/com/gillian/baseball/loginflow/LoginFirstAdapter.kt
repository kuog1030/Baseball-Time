package com.gillian.baseball.loginflow

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class LoginFirstAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titleList = listOf("創建隊伍","認領球員資料")

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> PagerLoginCreate()
            else -> PagerLoginSearch()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}