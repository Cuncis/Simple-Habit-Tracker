package com.cuncisboss.simplehabittracker.ui.todo

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.cuncisboss.simplehabittracker.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_container_tabs_todo.*
import org.koin.android.ext.android.inject

class ContainerTabsTodoFragment : Fragment(R.layout.fragment_container_tabs_todo) {

    private val viewModel by inject<TodoViewModel>()
    private val pref by inject<SharedPreferences>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (savedInstanceState != null) {
//            val currentPos = savedInstanceState.getInt(CURRENT_POSITION_KEY)
//            tab_todo.getTabAt(currentPos)?.select()
//            pager_todo.setCurrentItem(currentPos, false)
//        }


        pager_todo.adapter = TodoViewStateAdapter(childFragmentManager, lifecycle)
        tab_todo.addTab(tab_todo.newTab().setText("Yesterday"))
        tab_todo.addTab(tab_todo.newTab().setText("Today"))
        tab_todo.addTab(tab_todo.newTab().setText("Tomorrow"))
        tab_todo.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                pager_todo.currentItem = tab?.position.toString().toInt()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })

        pager_todo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tab_todo.selectTab(tab_todo.getTabAt(position))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        tab_todo.getTabAt(1)?.select()
        pager_todo.setCurrentItem(1, false)
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putInt(CURRENT_POSITION_KEY, pager_todo.currentItem)
//        super.onSaveInstanceState(outState)
//    }
}