package com.cuncisboss.simplehabittracker.ui.todo

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.cuncisboss.simplehabittracker.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_container_tabs_todo.*
import java.util.concurrent.atomic.AtomicBoolean

class ContainerTabsTodoFragment : Fragment(R.layout.fragment_container_tabs_todo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

        pager_todo.adapter = ViewStateAdapter(parentFragmentManager, lifecycle)
        tab_todo.addTab(tab_todo.newTab().setText("Yesterday"))
        tab_todo.addTab(tab_todo.newTab().setText("Today"))
        tab_todo.addTab(tab_todo.newTab().setText("Tomorrow"))

        pager_todo.setCurrentItem(1, false)
        tab_todo.getTabAt(1)?.select()

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

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.getItem(0).isVisible = false
    }
}