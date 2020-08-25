package com.cuncisboss.simplehabittracker.ui.reward

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cuncisboss.simplehabittracker.ui.reward.available.AvailableFragment
import com.cuncisboss.simplehabittracker.ui.reward.claimed.ClaimedFragment


class RewardViewStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AvailableFragment()
            }
            else -> {
                ClaimedFragment()
            }
        }
    }
}