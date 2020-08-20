package com.cuncisboss.simplehabittracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        bottom_navigation_view.setupWithNavController(nav_host_fragment.findNavController())
        bottom_navigation_view.setOnNavigationItemReselectedListener { /* NO-OP */ }
        nav_host_fragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.dashboardFragment -> {
                        tv_toolbar_title.text = getString(R.string.dashboard)
                    }
                    R.id.containerTabsTodoFragment -> {
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
}