package com.cuncisboss.simplehabittracker.ui.todo

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.util.Constants
import com.cuncisboss.simplehabittracker.util.Constants.KEY_CURRENT_DATE
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_TODAY
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_TOMORROW
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_YESTERDAY
import com.cuncisboss.simplehabittracker.util.Helper
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_container_tabs_todo.*
import org.koin.android.ext.android.inject
import java.util.concurrent.atomic.AtomicBoolean

class ContainerTabsTodoFragment : Fragment(R.layout.fragment_container_tabs_todo) {

    private val viewModel by inject<TodoViewModel>()
    private val pref by inject<SharedPreferences>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

        if (pref.getString(KEY_CURRENT_DATE, "") != "") {
            if (Helper.checkIsToday(pref.getString(KEY_CURRENT_DATE, "").toString()) == 1) {    // today
                Toast.makeText(requireContext(), "nothing because today", Toast.LENGTH_SHORT).show()
            } else {
                // tomorrow -> TODAY
                viewModel.updateAllTaskByDate(
                    Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(0)),
                    TASK_TYPE_TOMORROW,     // old
                    TASK_TYPE_TODAY         // new
                )
                // today - YESTERDAY
                viewModel.updateAllTaskByDate(
                    Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(-1)),
                    TASK_TYPE_TODAY,        // old
                    TASK_TYPE_YESTERDAY     // new
                )
            }
        }

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