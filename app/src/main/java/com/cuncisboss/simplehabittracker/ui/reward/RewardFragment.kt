package com.cuncisboss.simplehabittracker.ui.reward

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.util.Constants.KEY_AVAILABLE
import com.cuncisboss.simplehabittracker.util.Constants.KEY_CLAIMED
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_reward.*


class RewardFragment : Fragment(R.layout.fragment_reward) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager_reward.adapter = RewardViewStateAdapter(childFragmentManager, lifecycle)
        tab_reward.addTab(tab_reward.newTab().setText(KEY_AVAILABLE))
        tab_reward.addTab(tab_reward.newTab().setText(KEY_CLAIMED))
        tab_reward.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                pager_reward.currentItem = tab?.position.toString().toInt()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })

        pager_reward.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tab_reward.selectTab(tab_reward.getTabAt(position))
            }
        })
    }
}