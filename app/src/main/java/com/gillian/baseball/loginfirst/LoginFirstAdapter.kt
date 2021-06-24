package com.gillian.baseball.loginfirst

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gillian.baseball.R
import com.gillian.baseball.util.Util.getString

class LoginFirstAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titleList = listOf(getString(R.string.login_pager_create), getString(R.string.login_pager_search))

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