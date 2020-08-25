package com.cuncisboss.simplehabittracker.ui.reward

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.cuncisboss.simplehabittracker.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_reward.*


class RewardFragment : Fragment(R.layout.fragment_reward) {

//    private val pref by inject<SharedPreferences>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        tv_text_sample.text = pref.getLong(KEY_TOTAL, 0L).toString()

        pager_reward.adapter = RewardViewStateAdapter(childFragmentManager, lifecycle)
        tab_reward.addTab(tab_reward.newTab().setText("Available"))
        tab_reward.addTab(tab_reward.newTab().setText("Claimed"))
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