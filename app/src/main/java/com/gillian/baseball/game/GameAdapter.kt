package com.gillian.baseball.game

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gillian.baseball.game.batting.BattingFragment

class GameAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when ( position % 2 ) {
            0 -> BattingFragment()
            else -> BattingFragment()
        }
    }

    override fun getCount(): Int = 2

}


//class CatalogAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//    override fun getItem(position: Int): Fragment {
//        return CatalogItemFragment(CatalogTypeFilter.values()[position])
//    }
//
//    override fun getCount() = CatalogTypeFilter.values().size
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return CatalogTypeFilter.values()[position].value
//    }
//}