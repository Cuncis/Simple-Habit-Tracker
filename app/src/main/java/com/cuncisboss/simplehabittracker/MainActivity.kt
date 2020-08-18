package com.cuncisboss.simplehabittracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.cuncisboss.simplehabittracker.service.ReminderService
import com.cuncisboss.simplehabittracker.util.Constants.ACTION_SHOW_TODO_FRAGMENT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        navigateToTodoFragmentIfNeeded(intent)

        setSupportActionBar(toolbar)
        bottom_navigation_view.setupWithNavController(nav_host_fragment.findNavController())
        bottom_navigation_view.setOnNavigationItemReselectedListener { /* NO-OP */ }
        nav_host_fragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.dashboardFragment -> {
                        tv_toolbar_title.text = getString(R.string.dashboard)
                    }
                    R.id.todoFragment -> {
                        tv_toolbar_title.text = getString(R.string.todo)
                    }
                    R.id.rewardFragment -> {
                        tv_toolbar_title.text = getString(R.string.reward)
                    }
                    R.id.settingFragment -> {
                        tv_toolbar_title.text = getString(R.string.setting)
                    }
                }
            }
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        navigateToTodoFragmentIfNeeded(intent)
//    }
//
//    private fun navigateToTodoFragmentIfNeeded(intent: Intent?) {
//        if (intent?.action == ACTION_SHOW_TODO_FRAGMENT) {
//            Toast.makeText(this, "Clicked from notif to todo fragment", Toast.LENGTH_SHORT).show()
////            nav_host_fragment.findNavController().navigate()
//        }
//    }
}