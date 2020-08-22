package com.cuncisboss.simplehabittracker

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
import com.cuncisboss.simplehabittracker.util.Constants
import com.cuncisboss.simplehabittracker.util.Constants.KEY_CURRENT_DATE
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_YESTERDAY
import com.cuncisboss.simplehabittracker.util.Helper
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val pref by inject<SharedPreferences>()
    private val viewModel by inject<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (pref.getString(KEY_CURRENT_DATE, "") == "") {
            pref.edit().putString(KEY_CURRENT_DATE, Helper.getCurrentDatetime(0)).apply()
            Toast.makeText(this, "new date added", Toast.LENGTH_SHORT).show()
        } else {
            if (Helper.checkIsToday(pref.getString(KEY_CURRENT_DATE, "").toString()) == 1) {    // today
                Toast.makeText(this, "nothing because today", Toast.LENGTH_SHORT).show()
            } else {
                pref.edit().putString(KEY_CURRENT_DATE, Helper.getCurrentDatetime(0)).apply()     // yesterday++

                // yesterday
                viewModel.removeTaskByType(TASK_TYPE_YESTERDAY)
                Toast.makeText(this, "update date", Toast.LENGTH_SHORT).show()
            }
        }

        setSupportActionBar(toolbar)
        bottom_navigation_view.setupWithNavController(nav_host_fragment.findNavController())
        bottom_navigation_view.setOnNavigationItemReselectedListener { /* NO-OP */ }
        nav_host_fragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.dashboardFragment -> {
                        tv_toolbar_title.text = getString(R.string.dashboard)
                        toolbar.menu.clear()
                    }
                    R.id.containerTabsTodoFragment -> {
                        tv_toolbar_title.text = getString(R.string.todo)
                    }
                    R.id.rewardFragment -> {
                        tv_toolbar_title.text = getString(R.string.reward)
                        toolbar.menu.clear()
                    }
                    R.id.settingFragment -> {
                        tv_toolbar_title.text = getString(R.string.setting)
                        toolbar.menu.clear()
                    }
                }
            }
    }
}