package com.cuncisboss.simplehabittracker.ui.todo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cuncisboss.simplehabittracker.ui.todo.today.TodayFragment
import com.cuncisboss.simplehabittracker.ui.todo.tomorrow.TomorrowFragment
import com.cuncisboss.simplehabittracker.ui.todo.yesterday.YesterdayFragment


class TodoViewStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        // Hardcoded in this order, you'll want to use lists and make sure the titles match
        return when (position) {
            0 -> {
                YesterdayFragment()
            }
            1 -> {
                TodayFragment()
            }
            else -> {
                TomorrowFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        // Hardcoded, use lists
        return 3
    }
}